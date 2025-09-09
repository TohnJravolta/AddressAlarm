package org.flagdrive.data

import androidx.room.TypeConverter

object Converters {
    private const val SEP = "|||" // unlikely in real tags

    @TypeConverter
    @JvmStatic
    fun fromTags(list: List<String>?): String =
        list?.joinToString(SEP) ?: ""

    @TypeConverter
    @JvmStatic
    fun toTags(str: String?): List<String> =
        (str ?: "").takeIf { it.isNotEmpty() }?.split(SEP)?.map { it.trim() } ?: emptyList()
}