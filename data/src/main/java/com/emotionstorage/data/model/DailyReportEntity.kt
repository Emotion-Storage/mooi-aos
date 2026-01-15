package com.emotionstorage.data.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyReportEntity(
    val id: Long,
    val date: LocalDate,
    val isOpen: Boolean = false,
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
        val emotion: String,
        val description: String,
        val time: LocalDateTime,
    )
}
