package com.emotionstorage.remote.response.timeCapsule
import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class GetTimeCapsuleDetailResponse(
    val id: Long,
    // 타임캡슐 대화 시작 시간
    @Serializable(with = LocalDateTimeSerializer::class)
    val historyDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val updatedAt: LocalDateTime,
    val status: String,
    // 타임캡슐 오픈일 - 임시저장 상태인 경우, null
    @Serializable(with = LocalDateTimeSerializer::class)
    val openAt: LocalDateTime? = null,
    val isFavorite: Boolean,
    val title: String,
    val summary: String,
    val emotionDetails: List<EmotionDetail>,
    val comments: List<String>,
    val note: String? = null,
) {
    @Serializable
    data class EmotionDetail(
        val label: String,
        val ratio: Int,
    )
}
