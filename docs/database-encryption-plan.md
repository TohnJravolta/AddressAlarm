# Database Encryption Plan

## Current Storage
- The app uses Android Room to persist `FlaggedPlace` entities in a plain-text SQLite database file named `flagdrive.db`.
- The database is built via `Room.databaseBuilder` without any encryption-related configuration, so data is stored unencrypted on disk.

## Immediate Encryption Strategy

1. **Add SQLCipher for Room**
   - Add the `net.zetetic:android-database-sqlcipher` dependency and Room's `room-ktx` if not already present.
   - Update the Room database builder to use a `SupportFactory` backed by SQLCipher so Room reads/writes encrypted data.

2. **Generate a Per-Device Database Key**
   - On first launch, generate a random 256-bit key using `SecureRandom`.
   - Protect the key with Android's `KeyStore` (e.g., create an AES key with `setUserAuthenticationRequired(true)` if biometric/PIN gating is desired).
   - Encrypt the random database key with the KeyStore-backed key and store the ciphertext in `SharedPreferences`.

3. **Integrate the Encrypted Factory**
   - Retrieve and decrypt the stored database key at app startup.
   - Instantiate the Room database using `SupportFactory(databaseKey)` so SQLCipher handles encryption transparently.

4. **Migration of Existing Data**
   - Detect whether an unencrypted database exists; if so, open it, read all `FlaggedPlace` rows, and insert them into the encrypted database before deleting the plain-text file.
   - Provide user messaging or a one-time background worker to perform the migration safely.

5. **Hardening & Testing**
   - Add instrumentation tests to confirm the database file is unreadable without the generated key.
   - Verify backup/restore behavior; encrypted DB should still be restorable only on the originating device due to KeyStore-stored key.

6. **Future Enhancements**
   - Consider periodically rotating the database key and re-encrypting data.
   - Evaluate exporting/importing flows to ensure they respect encryption requirements.

## Single-Prompt Execution Checklist

These are the concrete edits a single Codex agent can apply in one prompt to ship encrypted storage immediately:

1. **Add dependencies**
   - Append `implementation("net.zetetic:android-database-sqlcipher:4.5.5")` and `implementation("androidx.sqlite:sqlite-ktx:2.4.0")` to `app/build.gradle.kts`.
   - Enable Room's incremental annotation processing if it is not already on to keep build times predictable.

2. **Create a key helper**
   - Add `EncryptionKeyProvider.kt` in `app/src/main/java/.../data/` that lazily generates a 256-bit key with `SecureRandom`, protects it with an AES key in the Android Keystore, and exposes a `getOrCreateDatabaseKey(): ByteArray` method.
   - Store the wrapped key bytes in `EncryptedSharedPreferences` so only the originating device can unwrap it.

3. **Wire SQLCipher into Room**
   - Update the Room database builder (likely in `FlaggedPlaceDatabase.kt`) to instantiate `SupportFactory(encryptionKeyProvider.getOrCreateDatabaseKey())` and pass it to `Room.databaseBuilder`.
   - Ensure the open-helper configuration uses `setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)` because SQLCipher requires it for best performance.

4. **Migrate plaintext data**
   - Write a one-off `Migration(1, 2)` class that reads the existing `flagdrive.db`, copies rows into the encrypted instance via `INSERT INTO flaggedplace SELECT * FROM flaggedplace`, and deletes the legacy file with `context.getDatabasePath("flagdrive.db").delete()` after success.
   - Register the migration on the builder and bump the Room schema version.

5. **Verification hooks**
   - Add an instrumentation test that opens the database, closes it, and asserts the underlying file contains high-entropy bytes (e.g., via a quick entropy check or attempting to open with a plain SQLite helper and expecting failure).
   - Document the need for QA to confirm backup/restore still succeeds on the same device but fails on a different device without the wrapped key.

With the above edits gathered into one prompt, Codex can apply the diff, run the Gradle formatter if required, and deliver encrypted storage in a single reviewable change.

