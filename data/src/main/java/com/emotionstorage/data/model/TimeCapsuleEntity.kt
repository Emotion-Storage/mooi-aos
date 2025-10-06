package com.emotionstorage.data.model

import java.time.LocalDateTime

data class TimeCapsuleEntity(
    val id: String,
    val status: String,
    val title: String,
    val summary: String,
    val isFavorite: Boolean = false,
    val emotions: List<Emotion> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String? = null,
    val createdAt: LocalDateTime,
    val arriveAt: LocalDateTime? = null,
    val favoriteAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    data class Emotion(
        val emotion: String,
        val percentage: Float? = null,
    )
}
