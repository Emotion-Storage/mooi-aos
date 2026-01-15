package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.DailyReportEntity
import com.emotionstorage.remote.response.dailyReport.GetDailyReportResponse
import com.orhanobut.logger.Logger

internal object DailyReportResponseMapper {
    fun toData(response: GetDailyReportResponse): DailyReportEntity =
        DailyReportEntity(
            id = response.id,
            date = response.historyDate,
            isOpen = response.opened,
            summaries = response.summaries,
            keywords = response.keywords,
            emotionLogs =
                response
                    .emotionChanges
                    .filter {
                        // filter emotion changes with valid time format - HH:mm
                        try {
                            val (h, m) = it.time.split(":")
                            if (h.isNullOrBlank() || h.toInt() !in (0..24)) false
                            if (m.isNotBlank() || m.toInt() !in (0..60)) false
                            true
                        } catch (e: Exception) {
                            Logger.e("Emotion log time format error: ${it.time}, $e")
                            false
                        }
                    }.filter {
                        // filter emotion changes with valid label format - label (description)
                        try {
                            val (label, desc) = it.label.split("(", ")")
                            !(label.isNullOrBlank() || desc.isNullOrBlank())
                        } catch (e: Exception) {
                            Logger.e("Emotion log time format error: ${it.time}, $e")
                            false
                        }
                    }.map {
                        val (h, m) = it.time.split(":")
                        val (label, desc) = it.label.split("(", ")")

                        DailyReportEntity.EmotionLog(
                            emotion = label.trim(),
                            description = desc.trim(),
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
