package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.local.model.TimeCapsuleLocal

internal object TimeCapsuleMapper{
    fun toLocal(entity: TimeCapsuleEntity) =
        TimeCapsuleLocal(
            id = entity.id,
            status = entity.status,
            title = entity.title,
            summary = entity.summary,
            isFavorite = entity.isFavorite,
            emotions = entity.emotions.map {
                it.emotion to it.percentage
            },
            comments = entity.comments,
            note = entity.note,
            historyDate = entity.createdAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            openAt = entity.openAt,
            favoriteAt = entity.favoriteAt,
        )

    fun toData(local: TimeCapsuleLocal) =
        TimeCapsuleEntity(
            id = local.id,
            status = local.status,
            title = local.title,
            summary = local.summary,
            isFavorite = local.isFavorite,
            emotions = local.emotions.map { (emotion, percentage) ->
                TimeCapsuleEntity.Emotion(
                    emotion = emotion,
                    percentage = percentage,
                )
            },
            comments = local.comments,
            note = local.note,
            historyDate = local.historyDate,
            createdAt = local.createdAt,
            updatedAt = local.updatedAt,
            openAt = local.openAt,
            favoriteAt = local.favoriteAt,
        )
}
