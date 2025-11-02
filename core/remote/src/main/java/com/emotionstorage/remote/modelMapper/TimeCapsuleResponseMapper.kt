package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.data.model.TimeCapsuleEntity.PageData
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
                historyDate = it.historyDate,
                createdAt = it.createdAt,
                openAt = it.openAt,
                updatedAt = it.updatedAt,
                pageData =
                    PageData(
                        page = response.pagination.page,
                        hasNextPage = response.pagination.page < response.pagination.totalPage,
                    ),
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
            historyDate = response.historyDate,
            createdAt = response.createdAt,
            openAt = response.openAt,
            updatedAt = response.updatedAt,
        )
}
