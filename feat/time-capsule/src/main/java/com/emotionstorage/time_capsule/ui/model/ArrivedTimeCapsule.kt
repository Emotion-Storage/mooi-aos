package com.emotionstorage.time_capsule.ui.model

import java.time.LocalDateTime

data class ArrivedTimeCapsule(
    val id: String,
    val title: String,
    val emotions: List<String> = emptyList(),
    val arriveAt: LocalDateTime,
)