package com.emotionstorage.remote.response.timeCapsule
import com.emotionstorage.common.util.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GetTimeCapsuleDetailResponse(
    val id: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val historyDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    val status: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val openAt: LocalDateTime,
    val isFavorite: Boolean,
    val title: String,
    val summary: String,
    val emotionDetails: List<EmotionDetail>,
    val comments: List<String>,
    val note: String,
) {
    @Serializable
    data class EmotionDetail(
        val label: String,
        val ratio: Int,
    )
}
