package com.emotionstorage.time_capsule.ui.model

import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import java.time.LocalDateTime

data class TimeCapsuleItemState(
    val id: Long,
    val status: TimeCapsule.Status,
    val title: String,
    val emotions: List<Emotion> = emptyList(),
    val isFavorite: Boolean = false,
    val isFavoriteAt: LocalDateTime? = null,
    val createdAt: LocalDateTime,
    // expireAt null if not TEMPORARY
    val expireAt: LocalDateTime? = null,
    // open d-day null if TEMPORARY
    val openDDay: Int? = null,
)
