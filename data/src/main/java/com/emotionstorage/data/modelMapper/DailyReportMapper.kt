package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.DailyReportEntity
import com.emotionstorage.domain.model.DailyReport

internal object DailyReportMapper {
    fun toDomain(entity: DailyReportEntity): DailyReport =
        DailyReport(
            id = entity.id,
            date = entity.date,
            isOpen = entity.isOpen,
            summaries = entity.summaries,
            keywords = entity.keywords,
            emotionLogs =
                entity
                    .emotionLogs
                    .filter {
                        // check emotion string format
                        (it.emotion.split(" ")).size == 2
                    }.map {
                        val (emoji, label) = it.emotion.split(" ")
                        DailyReport.EmotionLog(
                            emoji = emoji,
                            label = label,
                            description = it.description,
                            time = it.time,
                        )
                    },
            stressScore = entity.stressScore,
            happinessScore = entity.happinessScore,
            emotionSummary = entity.emotionSummary,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
