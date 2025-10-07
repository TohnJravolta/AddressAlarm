package org.flagdrive.data

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Provides the SQLCipher database key, generating and wrapping it with an AES key stored in the
 * Android Keystore. The wrapped bytes are persisted in EncryptedSharedPreferences so the key never
 * leaves the device.
 */
class EncryptionKeyProvider private constructor(context: Context) {

    private val appContext = context.applicationContext
    private val prefs: SharedPreferences by lazy { encryptedPreferences() }

    fun getOrCreateDatabaseKey(): ByteArray = synchronized(LOCK) {
        val wrappingKey = getOrCreateWrappingKey()
        val encoded = prefs.getString(PREF_KEY_WRAPPED_DB_KEY, null)
        if (encoded != null) {
            return@synchronized unwrapKey(encoded, wrappingKey)
        }

        val newKey = ByteArray(DB_KEY_SIZE_BYTES).apply { SecureRandom().nextBytes(this) }
        val wrapped = wrapKey(newKey, wrappingKey)
        prefs.edit().putString(PREF_KEY_WRAPPED_DB_KEY, wrapped).apply()
        newKey
    }

    private fun encryptedPreferences(): SharedPreferences {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            appContext,
            PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getOrCreateWrappingKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        val existing = keyStore.getKey(KEY_ALIAS, null) as? SecretKey
        if (existing != null) {
            return existing
        }

        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val parameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(DB_KEY_SIZE_BITS)
            .build()
        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    private fun wrapKey(plainKey: ByteArray, wrappingKey: SecretKey): String {
        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, wrappingKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plainKey)

        val payload = ByteBuffer.allocate(IV_LENGTH_HEADER_BYTES + iv.size + encrypted.size)
            .order(ByteOrder.BIG_ENDIAN)
            .putInt(iv.size)
            .put(iv)
            .put(encrypted)
            .array()
        return Base64.encodeToString(payload, Base64.NO_WRAP)
    }

    private fun unwrapKey(encoded: String, wrappingKey: SecretKey): ByteArray {
        val payload = Base64.decode(encoded, Base64.NO_WRAP)
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.BIG_ENDIAN)
        val ivSize = buffer.int
        val iv = ByteArray(ivSize).also { buffer.get(it) }
        val cipherText = ByteArray(buffer.remaining()).also { buffer.get(it) }

        val cipher = Cipher.getInstance(AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, wrappingKey, GCMParameterSpec(GCM_AUTH_TAG_LENGTH_BITS, iv))
        return cipher.doFinal(cipherText)
    }

    companion object {
        private const val PREF_FILE_NAME = "flagdrive.encryption"
        private const val PREF_KEY_WRAPPED_DB_KEY = "wrapped_db_key"
        private const val KEY_ALIAS = "flagdrive.db.wrapper"
        private const val DB_KEY_SIZE_BITS = 256
        private const val DB_KEY_SIZE_BYTES = DB_KEY_SIZE_BITS / 8
        private const val AES_GCM_TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_AUTH_TAG_LENGTH_BITS = 128
        private const val IV_LENGTH_HEADER_BYTES = 4
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"

        private val LOCK = Any()

        @Volatile
        private var instance: EncryptionKeyProvider? = null

        fun getInstance(context: Context): EncryptionKeyProvider {
            return instance ?: synchronized(this) {
                instance ?: EncryptionKeyProvider(context).also { instance = it }
            }
        }
    }
}
