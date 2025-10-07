package org.flagdrive.data

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import java.io.File

/**
 * Handles migrating the legacy plaintext Room database into the encrypted SQLCipher database.
 * Data is staged in memory and the original file is kept as a backup until the migration succeeds.
 */
class PlaintextDatabaseMigrator(context: Context) {

    private val appContext = context.applicationContext
    private val originalFile = appContext.getDatabasePath(DATABASE_NAME)
    private val statePrefs: SharedPreferences =
        appContext.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

    /**
     * Extracts rows from the legacy plaintext database if it exists and has not already been migrated.
     */
    fun extractLegacyDataIfNeeded(): LegacyDump? {
        if (statePrefs.getBoolean(PREF_KEY_MIGRATED, false)) {
            return null
        }

        if (!originalFile.exists()) {
            return null
        }

        return try {
            val places = readLegacyRows(originalFile)
            val backupFile = stageLegacyFile(originalFile)
            LegacyDump(places, backupFile)
        } catch (ex: SQLiteException) {
            Log.w(TAG, "Skipping plaintext migration: ${ex.message}")
            statePrefs.edit().putBoolean(PREF_KEY_MIGRATED, true).apply()
            null
        }
    }

    /**
     * Inserts legacy data into the encrypted database and cleans up the staged plaintext file.
     * This must be called from within a transaction on the target database, such as from
     * RoomDatabase.Callback#onCreate.
     */
    fun importLegacyData(writableDb: androidx.sqlite.db.SupportSQLiteDatabase, dump: LegacyDump) {
        if (dump.places.isEmpty()) {
            cleanupBackup(dump)
            return
        }

        try {
            val statement = writableDb.compileStatement(INSERT_OR_REPLACE_SQL)
            dump.places.forEach { place ->
                statement.bindLong(1, place.id)
                if (place.name != null) {
                    statement.bindString(2, place.name)
                } else {
                    statement.bindNull(2)
                }
                statement.bindString(3, place.rawAddress)
                statement.bindString(4, Converters.fromTags(place.tags))
                if (place.notes != null) {
                    statement.bindString(5, place.notes)
                } else {
                    statement.bindNull(5)
                }
                statement.executeInsert()
                statement.clearBindings()
            }
        } finally {
            // The transaction is managed by Room, but we must clean up the backup file.
            cleanupBackup(dump)
        }
    }

    fun markEncrypted() {
        statePrefs.edit().putBoolean(PREF_KEY_MIGRATED, true).apply()
    }

    private fun readLegacyRows(file: File): List<FlaggedPlace> {
        val results = mutableListOf<FlaggedPlace>()
        SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY).use { legacyDb ->
            legacyDb.rawQuery(LEGACY_SELECT_SQL, null).use { cursor ->
                val idIndex = cursor.getColumnIndexOrThrow("id")
                val nameIndex = cursor.getColumnIndexOrThrow("name")
                val rawAddressIndex = cursor.getColumnIndexOrThrow("rawAddress")
                val tagsIndex = cursor.getColumnIndexOrThrow("tags")
                val notesIndex = cursor.getColumnIndexOrThrow("notes")

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idIndex)
                    val name = if (!cursor.isNull(nameIndex)) cursor.getString(nameIndex) else null
                    val rawAddress = cursor.getString(rawAddressIndex)
                    val tagsString = if (!cursor.isNull(tagsIndex)) cursor.getString(tagsIndex) else ""
                    val notes = if (!cursor.isNull(notesIndex)) cursor.getString(notesIndex) else null
                    results.add(
                        FlaggedPlace(
                            id = id,
                            name = name,
                            rawAddress = rawAddress,
                            tags = Converters.toTags(tagsString),
                            notes = notes
                        )
                    )
                }
            }
        }
        return results
    }

    private fun stageLegacyFile(file: File): File {
        val backupFile = File(file.parentFile, "${file.name}.$LEGACY_BACKUP_SUFFIX")
        if (backupFile.exists()) {
            backupFile.delete()
        }

        if (!file.renameTo(backupFile)) {
            file.copyTo(backupFile, overwrite = true)
            if (!file.delete()) {
                throw IllegalStateException("Unable to remove legacy database file ${file.path}")
            }
        }
        return backupFile
    }

    private fun cleanupBackup(dump: LegacyDump) {
        if (dump.backupFile.exists() && !dump.backupFile.delete()) {
            Log.w(TAG, "Failed to delete legacy database backup at ${dump.backupFile.path}")
        }
    }

    data class LegacyDump(
        val places: List<FlaggedPlace>,
        val backupFile: File
    )

    companion object {
        private const val TAG = "PlaintextMigrator"
        private const val DATABASE_NAME = "flagdrive.db"
        private const val PREF_FILE_NAME = "flagdrive.migration.state"
        private const val PREF_KEY_MIGRATED = "database_encrypted"
        private const val LEGACY_BACKUP_SUFFIX = "legacy"
        private const val LEGACY_SELECT_SQL =
            "SELECT id, name, rawAddress, tags, notes FROM flagged_places ORDER BY id ASC"
        private const val INSERT_OR_REPLACE_SQL =
            "INSERT OR REPLACE INTO flagged_places (id, name, rawAddress, tags, notes) VALUES (?, ?, ?, ?, ?)"
    }
}
