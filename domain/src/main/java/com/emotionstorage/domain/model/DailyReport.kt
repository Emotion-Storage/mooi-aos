package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class DailyReport(
    val id: String,
    val summaries: List<String> = emptyList(),
    val keywords: List<String> = emptyList(),
    val emotionLogs: List<EmotionLog> = emptyList(),
    val stressScore: Int = 0,
    val happinessScore: Int = 0,
    val emotionSummary: String = "",
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    ) {
    data class EmotionLog(
        val emoji: String,
        val label: String,
        val description: String,
        val time: LocalDateTime
    )
}
