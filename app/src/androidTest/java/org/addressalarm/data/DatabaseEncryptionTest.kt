package org.addressalarm.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import net.sqlcipher.database.SQLiteDatabase as SQLCipherDatabase
import net.sqlcipher.database.SupportFactory
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DatabaseEncryptionTest {

    private lateinit var context: Context
    private lateinit var dbName: String
    private val keyProvider by lazy { EncryptionKeyProvider.getInstance(context) }

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        dbName = "encryption-test.db"
        val dbFile = context.getDatabasePath(dbName)
        if (dbFile.exists()) {
            dbFile.delete()
        }
        SQLCipherDatabase.loadLibs(context)
    }

    @After
    fun tearDown() {
        val dbFile = context.getDatabasePath(dbName)
        if (dbFile.exists()) {
            dbFile.delete()
        }
    }

    @Test
    fun openingWithPlainSQLiteFails() {
        val passphrase = keyProvider.getOrCreateDatabaseKey()
        val factory = SupportFactory(passphrase.copyOf())

        val database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dbName
        )
            .openHelperFactory(factory)
            .setJournalMode(androidx.room.RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()

        database.close()
        passphrase.fill(0)

        val dbFile = context.getDatabasePath(dbName)
        assertTrue(dbFile.exists())

        assertFailsWith<SQLiteException> {
            SQLiteDatabase.openDatabase(dbFile.path, null, SQLiteDatabase.OPEN_READONLY)
        }
    }
}
