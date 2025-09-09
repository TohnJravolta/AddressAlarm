package org.flagdrive.di

import android.content.Context
import androidx.room.Room
import org.flagdrive.data.AppDatabase
import org.flagdrive.data.PlacesRepository

object ServiceLocator {
    @Volatile private var db: AppDatabase? = null
    @Volatile private var repo: PlacesRepository? = null

    fun repository(context: Context): PlacesRepository {
        val database = db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "flagdrive.db"
            ).build().also { db = it }
        }

        return repo ?: synchronized(this) {
            repo ?: PlacesRepository(database.flaggedPlaceDao()).also { repo = it }
        }
    }
}