package org.addressalarm.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.addressalarm.data.AppDatabase
import org.addressalarm.data.EncryptionKeyProvider
import org.addressalarm.data.PlaintextDatabaseMigrator
import org.addressalarm.data.PlacesRepository

object ServiceLocator {
    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: PlacesRepository? = null
    @Volatile private var keyProvider: EncryptionKeyProvider? = null

    fun repository(context: Context): PlacesRepository {
        val appContext = context.applicationContext
        val database = db ?: synchronized(this) {
            db ?: buildDatabase(appContext).also { db = it }
        }

        return repo ?: synchronized(this) {
            repo ?: PlacesRepository(database.flaggedPlaceDao()).also { repo = it }
        }
    }

    private fun buildDatabase(context: Context): AppDatabase {
        val provider = keyProvider ?: EncryptionKeyProvider.getInstance(context).also { keyProvider = it }
        val migrator = PlaintextDatabaseMigrator(context)

        // Check for legacy data *before* building the database.
        // This is a small, synchronous I/O hit to check for a file and a shared preference.
        // It's a reasonable trade-off to correctly configure the one-time migration callback.
        val legacyDump = migrator.extractLegacyDataIfNeeded()

        SQLiteDatabase.loadLibs(context)
        val passphrase = provider.getOrCreateDatabaseKey()
        val factory = SupportFactory(passphrase.copyOf())

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .openHelperFactory(factory)
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                    super.onCreate(db)
                    // When the encrypted database is first created, import legacy data if it exists.
                    // This runs within Room's transaction.
                    legacyDump?.let { dump ->
                        migrator.importLegacyData(db, dump)
                    }
                }
            })
            .build()

        // Clear the key from memory after it has been passed to the factory.
        passphrase.fill(0)

        // Finalize the migration. If no migration was needed, this is a no-op besides marking
        // the migration as complete.
        migrator.onMigrationComplete(legacyDump)

        return db
    }

    private const val DATABASE_NAME = "addressalarm.db"
}
