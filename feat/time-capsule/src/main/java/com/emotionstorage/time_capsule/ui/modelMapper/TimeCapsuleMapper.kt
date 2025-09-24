package com.emotionstorage.time_capsule.ui.modelMapper

import com.emotionstorage.time_capsule.ui.model.TimeCapsuleState

object TimeCapsuleMapper {
    fun toUi(domain: TimeCapsuleState): TimeCapsuleState {
        return TimeCapsuleState(
            id = domain.id,
            title = domain.title,
            emotions = domain.emotions,
            isFavorite = domain.isFavorite,
            isFavoriteAt = domain.isFavoriteAt,
            createdAt = domain.createdAt
        )
    }
}