package com.emotionstorage.time_capsule.ui.model

import com.emotionstorage.domain.model.TimeCapsule.Emotion
import java.time.LocalDateTime

data class TimeCapsuleState(
    val id: String,
    val title: String,
    val emotions: List<Emotion> = emptyList(),
    val isFavorite: Boolean,
    val isFavoriteAt: LocalDateTime? = null,
    val createdAt: LocalDateTime,
)
