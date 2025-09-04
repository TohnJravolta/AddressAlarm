package org.flagdrive.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FlaggedPlace(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val rawAddress: String,
    val name: String? = null,
    // Stored as CSV by our TypeConverter
    val tags: List<String> = emptyList()
)
