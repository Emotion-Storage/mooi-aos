package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.StartEmotionConversationEntity
import com.emotionstorage.domain.model.EmotionConversation

object StartEmotionConversationMapper {
    fun toDomain(entity: StartEmotionConversationEntity): EmotionConversation =
        EmotionConversation(
            roomId = entity.roomId,
            isTempSave = entity.isTempSave,
            isFirstChatOfDay = entity.isFirstChatOfDay,
        )
}
