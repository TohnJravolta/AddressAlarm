package org.addressalarm.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlacesRepository(private val dao: FlaggedPlaceDao) {

    fun getAllFlow(): Flow<List<FlaggedPlace>> = dao.getAllFlow()

    // In-memory tag filtering (simpler than SQL over a serialized List)
    fun withTags(tags: List<String>): Flow<List<FlaggedPlace>> =
        if (tags.isEmpty()) dao.getAllFlow()
        else dao.getAllFlow().map { list ->
            list.filter { it.tags.any { t -> t in tags } }
        }

    suspend fun upsert(place: FlaggedPlace): Long {
        val existing = dao.findByAddressExact(place.rawAddress)
        return if (existing == null) {
            dao.insert(place)
        } else {
            // Ensure we update the existing record by using its ID
            dao.update(place.copy(id = existing.id))
            existing.id // Return the ID of the existing (now updated) record
        }
    }

    suspend fun delete(id: Long) = dao.delete(id)

    suspend fun findByAddressExact(address: String) = dao.findByAddressExact(address)

    suspend fun addSample() {
        upsert(FlaggedPlace(rawAddress = "123 Test St", name = "Test", tags = listOf("caution")))
    }
}