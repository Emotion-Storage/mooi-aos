package com.emotionstorage.remote.response.timeCapsule

import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class PatchTimeCapsuleFavoriteResponse(
    val isFavorite: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val favoriteAt: LocalDateTime,
    val favoritesCnt: Int,
)
