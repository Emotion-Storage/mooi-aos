package com.emotionstorage.local.room

import androidx.room.TypeConverter
import java.time.LocalDate
class DtoConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }
}
