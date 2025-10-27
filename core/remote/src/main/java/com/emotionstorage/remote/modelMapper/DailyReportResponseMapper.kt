package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.DailyReportEntity
import com.emotionstorage.remote.response.dailyReport.GetDailyReportResponse
import com.orhanobut.logger.Logger

internal object DailyReportResponseMapper {
    fun toData(response: GetDailyReportResponse): DailyReportEntity =
        DailyReportEntity(
            id = response.id,
            isOpen = response.isOpen,
            summaries = response.summaries,
            keywords = response.keywords,
            emotionLogs =
                response.emotionChanges.filter {
                    // filter emotion changes with valid time format
                    try {
                        val (h, m) = it.time.split(":")
                        if (h.isNullOrBlank() || h.toInt() !in (0..24)) false
                        if (m.isNotBlank() || m.toInt() !in (0..60)) false
                        true
                    } catch (e: Exception) {
                        Logger.e("Emotion log time format error: ${it.time}, $e")
                        false
                    }
                }.map {
                    val (h, m) = it.time.split(":")

                    DailyReportEntity.EmotionLog(
                        emotion = it.label,
                        description = it.description,
                        time = response.createdAt.withHour(h.toInt()).withMinute(m.toInt()),
                    )
                },
            stressScore = response.stressIndex,
            happinessScore = response.happinessIndex,
            emotionSummary = response.emotionSummary,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
        )
}
