package com.emotionstorage.time_capsule.ui.modelMapper

import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import java.time.LocalDateTime

object TimeCapsuleMapper {
    fun toUi(domain: TimeCapsule): TimeCapsuleItemState =
        TimeCapsuleItemState(
            id = domain.id,
            status = domain.status,
            title = domain.title,
            emotions = domain.emotions,
            isFavorite = domain.isFavorite,
            isFavoriteAt = domain.favoriteAt,
            createdAt = domain.createdAt,
            openDday =
                domain.arriveAt?.run {
                    this.dayOfYear - LocalDateTime.now().dayOfYear
                },
        )
}
