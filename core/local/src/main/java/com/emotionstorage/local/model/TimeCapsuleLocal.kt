package com.emotionstorage.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emotionstorage.common.LocalDateTimeSerializer
import com.emotionstorage.local.room.AppDatabaseConstant
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Entity(tableName = AppDatabaseConstant.TableName.TIME_CAPSULE_TABLE)
data class TimeCapsuleLocal(
    @PrimaryKey
    val id: Long,
    val status: String,
    val title: String,
    val summary: String = "",
    val isFavorite: Boolean = false,
    // emotion string = "emotion:percentage"
    val emotions: List<String> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String = "",
    @Serializable(with = LocalDateTimeSerializer::class)
    val historyDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val openAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val favoriteAt: LocalDateTime? = null,
)
