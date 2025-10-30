package com.emotionstorage.data.model

import java.time.LocalDateTime

data class TimeCapsuleEntity(
    val id: Long,
    val status: String,
    val title: String,
    val summary: String = "",
    val isFavorite: Boolean = false,
    val emotions: List<Emotion> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String = "",
    val historyDate: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val openAt: LocalDateTime? = null,
    val favoriteAt: LocalDateTime? = null,
) {
    data class Emotion(
        val emotion: String,
        val percentage: Float? = null,
    )
}
