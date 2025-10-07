package org.flagdrive.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.flagdrive.data.AppDatabase
import org.flagdrive.data.EncryptionKeyProvider
import org.flagdrive.data.PlaintextDatabaseMigrator
import org.flagdrive.data.PlacesRepository

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
        val legacyDump = migrator.extractLegacyDataIfNeeded()

        SQLiteDatabase.loadLibs(context)
        val passphrase = provider.getOrCreateDatabaseKey()
        val factory = SupportFactory(passphrase.copyOf())

        val database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .openHelperFactory(factory)
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .build()

        passphrase.fill(0)

        legacyDump?.let { migrator.importLegacyData(database, it) }
        migrator.markEncrypted()

        return database
    }

    private const val DATABASE_NAME = "flagdrive.db"
}
