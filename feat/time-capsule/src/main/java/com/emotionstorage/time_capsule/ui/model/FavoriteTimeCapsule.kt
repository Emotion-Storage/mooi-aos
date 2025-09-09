package com.emotionstorage.time_capsule.ui.model

import java.time.LocalDateTime

data class FavoriteTimeCapsule (
    val id: String,
    val title: String,
    val emotions: List<String> = emptyList(),
    val isFavorite: Boolean,
    val isFavoriteAt: LocalDateTime? = null,
    val createdAt: LocalDateTime,
)