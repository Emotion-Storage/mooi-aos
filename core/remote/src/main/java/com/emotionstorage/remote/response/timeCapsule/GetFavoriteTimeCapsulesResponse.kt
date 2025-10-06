package com.emotionstorage.remote.response.timeCapsule

import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GetFavoriteTimeCapsulesResponse(
    val pagination: PaginationInfo,
    val totalCapsules: Int,
    val timeCapsules: TimeCapsule,
) {
    @Serializable
    data class PaginationInfo(
        val page: Int,
        val limit: Int,
        val totalPage: Int,
    )

    @Serializable
    data class TimeCapsule(
        val id: String,
        @Serializable(with = LocalDateTimeSerializer::class)
        val historyDate: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        val createdAt: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        val updatedAt: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class)
        val openAt: LocalDateTime,
        val isFavorite: Boolean,
        val emotions: List<String>,
        val title: String,
        val status: String,
    )
}
