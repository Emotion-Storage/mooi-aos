package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.response.timeCapsule.GetTimeCapsuleDetailResponse
import com.emotionstorage.remote.response.timeCapsule.GetTimeCapsulesResponse

internal object TimeCapsuleResponseMapper {
    fun toData(response: GetTimeCapsulesResponse): List<TimeCapsuleEntity> =
        response.timeCapsules.map { it ->
            TimeCapsuleEntity(
                id = it.id,
                status = it.status,
                title = it.title,
                isFavorite = it.isFavorite,
                emotions =
                    it.emotions.map { emotion ->
                        TimeCapsuleEntity.Emotion(
                            emotion = emotion,
                        )
                    },
                createdAt = it.createdAt,
                arriveAt = it.openAt,
                updatedAt = it.updatedAt,
            )
        }

    fun toData(response: GetTimeCapsuleDetailResponse): TimeCapsuleEntity =
        TimeCapsuleEntity(
            id = response.id,
            status = response.status,
            title = response.title,
            summary = response.summary,
            isFavorite = response.isFavorite,
            emotions =
                response.emotionDetails.map { emotion ->
                    TimeCapsuleEntity.Emotion(
                        emotion = emotion.label,
                        percentage = emotion.ratio.toFloat(),
                    )
                },
            comments = response.comments,
            note = response.note,
            createdAt = response.createdAt,
            arriveAt = response.openAt,
            updatedAt = response.updatedAt,
        )
}
