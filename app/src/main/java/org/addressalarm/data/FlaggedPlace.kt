package org.addressalarm.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flagged_places")
data class FlaggedPlace(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String? = null,
    val rawAddress: String,
    val tags: List<String> = emptyList(),
    val notes: String? = null
)