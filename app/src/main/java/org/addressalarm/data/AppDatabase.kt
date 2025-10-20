package org.addressalarm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FlaggedPlace::class],
    version = 2,
    exportSchema = false // Changed to false to suppress warning
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flaggedPlaceDao(): FlaggedPlaceDao
}