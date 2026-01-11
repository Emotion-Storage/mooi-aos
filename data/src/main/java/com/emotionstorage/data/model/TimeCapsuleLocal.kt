package com.emotionstorage.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


// moved TimeCapsuleLocal to data module for PagingSource
@Entity(
    tableName = "time_capsule",
    indices = [
        Index(value = ["status"]),
        Index(value = ["isFavorite"]),
        Index(value = ["openAt"]),
    ],
)
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
