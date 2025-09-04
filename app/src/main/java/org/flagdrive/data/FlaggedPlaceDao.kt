package org.flagdrive.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FlaggedPlaceDao {

    // 👇 use an always-present column for ordering
    @Query("SELECT * FROM FlaggedPlace ORDER BY id DESC")
    suspend fun getAll(): List<FlaggedPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(place: FlaggedPlace): Long

    @Update
    suspend fun update(place: FlaggedPlace)

    @Delete
    suspend fun delete(place: FlaggedPlace)

    @Query("DELETE FROM FlaggedPlace WHERE id = :id")
    suspend fun deleteById(id: Long)
}
