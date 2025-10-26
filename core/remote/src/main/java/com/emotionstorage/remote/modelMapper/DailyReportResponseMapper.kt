package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.DailyReportEntity
import com.emotionstorage.remote.response.dailyReport.GetDailyReportResponse

internal object DailyReportResponseMapper {
    fun toData(response: GetDailyReportResponse): DailyReportEntity {
        return DailyReportEntity(
            id = response.id,
            isOpen = response.isOpen,
            summaries = response.summaries,
            keywords = response.keywords,
            emotionLogs =
                response.emotionChanges.map {
                    val (h, m) = it.time.split(":")

                    DailyReportEntity.EmotionLog(
                        emotion = it.label,
                        description = it.description,
                        time = response.createdAt.withHour(h.toInt()).withMinute(m.toInt())
                    )
                },
            stressScore = response.stressIndex,
            happinessScore = response.happinessIndex,
            emotionSummary = response.emotionSummary,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
        )
    }
}
