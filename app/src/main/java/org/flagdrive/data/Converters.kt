package org.flagdrive.data

import androidx.room.TypeConverter

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromCsv(csv: String?): List<String> {
        if (csv.isNullOrBlank()) return emptyList()
        return csv.split(',').mapNotNull { it.trim().ifBlank { null } }
    }

    @TypeConverter
    @JvmStatic
    fun toCsv(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}
