package com.emotionstorage.time_capsule.ui.model

import com.emotionstorage.domain.model.TimeCapsule.Emotion
import java.time.LocalDateTime

data class ArrivedTimeCapsule(
    val id: String,
    val title: String,
    val emotions: List<Emotion> = emptyList(),
    val arriveAt: LocalDateTime,
)