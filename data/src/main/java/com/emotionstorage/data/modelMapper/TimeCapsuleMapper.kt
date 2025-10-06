package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.domain.model.TimeCapsule

internal object TimeCapsuleMapper {
    fun toDomain(entity: TimeCapsuleEntity) =
        TimeCapsule(
            id = entity.id,
            status =
                if (entity.status == "임시 저장") {
                    TimeCapsule.STATUS.TEMPORARY
                } else if (entity.status == "잠김") {
                    TimeCapsule.STATUS.LOCKED
                } else if (entity.status == "도착") {
                    TimeCapsule.STATUS.ARRIVED
                } else if (entity.status == "열림") {
                    TimeCapsule.STATUS.OPENED
                } else {
                    throw IllegalArgumentException("Invalid status label")
                },
            title = entity.title,
            summary = entity.summary,
            isFavorite = entity.isFavorite,
            emotions =
                entity
                    .emotions
                    .filter {
                        // check emotion string format
                        (it.emotion.split(" ")).size == 2
                    }.map {
                        val (emoji, label) = it.emotion.split(" ")
                        TimeCapsule.Emotion(
                            emoji = emoji,
                            label = label,
                            percentage = it.percentage,
                        )
                    },
            comments = entity.comments,
            note = entity.note,
            createdAt = entity.createdAt,
            arriveAt = entity.arriveAt,
            favoriteAt = entity.favoriteAt,
            updatedAt = entity.updatedAt,
        )
}
