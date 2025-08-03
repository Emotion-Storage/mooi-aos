package com.emotionstorage.local.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

class DtoConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDataTime(date: LocalDateTime?): String? = date?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }
}
