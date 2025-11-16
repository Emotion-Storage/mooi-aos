package com.emotionstorage.remote.response.timeCapsule

import com.emotionstorage.data.model.TimeCapsuleEntity
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class CreateTimeCapsuleResponse(
    val title: String,
    val oneLineSummary: String,
    val dialogueSummary: String,
    val emotionKeywords: List<EmotionKeyword>,
    val aiFeedback: List<String>,
    val historyDate: String,
) {
    @Serializable
    data class EmotionKeyword(
        val label: String,
        val ration: Int,
    )
}

// TODO : 매핑 다시 고려해보기
fun CreateTimeCapsuleResponse.toEntity(
    id: Long,
    status: String,
    now: LocalDateTime = LocalDateTime.now(),
): TimeCapsuleEntity =
    TimeCapsuleEntity(
        id = id,
        status = status,
        title = title,
        summary = oneLineSummary,
        isFavorite = false,
        emotions =
            emotionKeywords.map {
                TimeCapsuleEntity.Emotion(
                    emotion = it.label,
                    percentage = it.ration / 100f,
                )
            },
        comments = aiFeedback,
        note = dialogueSummary,
        historyDate = now,
        createdAt = now,
        updatedAt = now,
        openAt = null,
        favoriteAt = null,
        pageData = null,
    )
