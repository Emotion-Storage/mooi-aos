package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.remote.response.timeCapsule.GetFavoriteTimeCapsulesResponse

internal object TimeCapsuleResponseMapper {
    fun toData(response: GetFavoriteTimeCapsulesResponse): List<TimeCapsuleEntity> =
        response.timeCapsules.map { it ->
            TimeCapsuleEntity(
                id = it.id,
                status = it.status,
                title = it.title,
                isFavorite = it.isFavorite,
                emotions = it.emotions.map { emotion ->
                    TimeCapsuleEntity.Emotion(
                        emotion = emotion
                    )
                },
                createdAt = it.createdAt,
                arriveAt = it.openAt,
                updatedAt = it.updatedAt
            )
        }
}

