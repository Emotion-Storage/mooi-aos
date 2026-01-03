package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.domain.model.EmotionChatSession

object StartEmotionConversationMapper {
    fun toDomain(entity: EmotionChatSessionEntity): EmotionChatSession =
        EmotionChatSession(
            roomId = entity.roomId,
            isTempSave = entity.isTempSave,
            isFirstChatOfDay = entity.isFirstChatOfDay,
        )
}
