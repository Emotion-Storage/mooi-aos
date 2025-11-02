package com.emotionstorage.local.room

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalDateTime

class DtoConverter {
    private val json =
        Json {
            ignoreUnknownKeys = true // Common configuration
            isLenient = true
            prettyPrint = true // For debugging, optional
        }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDataTime(date: LocalDateTime?): String? = date?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun fromStringList(value: List<String>): String =
        json.encodeToString(
            ListSerializer(String.serializer()),
            value,
        )

    @TypeConverter
    fun toStringList(value: String): List<String> = json.decodeFromString(value)
}
