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
                        try {
                            // hh:mm
                            val regex = "^(0\\d|1\\d|2[0-4])\\s*:\\s*(0\\d|[1-5]\\d|60)$".toRegex()
                            regex.matches(it.time)
                        } catch (e: Exception) {
                            Logger.e("emotionChanges time parsing error: ${it.time}, $e")
                            false
                        }
                    }.filter {
                        try {
                            // label (description)
                            val regex = "^[^()]+ \\([^()]+\\)$".toRegex()
                            regex.matches(it.label)
                        } catch (e: Exception) {
                            Logger.e("label parsing error: ${it.label}, $e")
                            false
                        }
                    }.map {
                        val (h, m) = it.time.split(":")
                        val (label, desc) = it.label.split("(", ")").dropLast(1)

                        DailyReportEntity.EmotionLog(
                            emotion = label.trim(),
                            description = desc.trim(),
                            time = response.createdAt.withHour(h.trim().toInt()).withMinute(m.trim().toInt()),
                        )
                    },
            stressScore = response.stressIndex,
            happinessScore = response.happinessIndex,
            emotionSummary = response.emotionSummary,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt,
        )
}
