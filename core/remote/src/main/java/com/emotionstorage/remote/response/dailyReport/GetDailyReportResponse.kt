package com.emotionstorage.remote.response.dailyReport

import com.emotionstorage.common.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GetDailyReportResponse(
    val id: Long,
    val isOpen: Boolean,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    val summaries: List<String>,
    val keywords: List<String>,
    val stressIndex: Int,
    val happinessIndex: Int,
    val emotionSummary: String,
    val emotionChanges: List<EmotionChange> = emptyList(),
) {
    @Serializable
    data class EmotionChange(
        // HH:mm
        val time: String,
        val label: String,
        val description: String,
    )
}
