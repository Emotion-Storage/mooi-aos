package com.emotionstorage.time_capsule.ui.model

import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import java.time.LocalDateTime

data class TimeCapsuleItemState(
    val id: String,
    val status: TimeCapsule.STATUS,
    val title: String,
    val emotions: List<Emotion> = emptyList(),
    val isFavorite: Boolean,
    val isFavoriteAt: LocalDateTime? = null,
    val createdAt: LocalDateTime,
    // open d-day null if TEMPORARY
    val openDday: Int? = null,
)
