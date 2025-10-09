package org.addressalarm.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlaggedPlaceDao {

    @Query("SELECT * FROM flagged_places ORDER BY id DESC")
    fun getAllFlow(): Flow<List<FlaggedPlace>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: FlaggedPlace): Long

    @Update
    suspend fun update(place: FlaggedPlace)

    @Query("DELETE FROM flagged_places WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM flagged_places WHERE rawAddress = :address LIMIT 1")
    suspend fun findByAddressExact(address: String): FlaggedPlace?
}