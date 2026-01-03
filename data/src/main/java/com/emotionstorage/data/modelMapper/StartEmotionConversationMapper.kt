package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.StartEmotionConversationEntity
import com.emotionstorage.domain.model.EmotionConversationStartInfo

object StartEmotionConversationMapper {
    fun toDomain(entity: StartEmotionConversationEntity): EmotionConversationStartInfo =
        EmotionConversationStartInfo(
            roomId = entity.roomId,
            isTempSave = entity.isTempSave,
            isFirstChatOfDay = entity.isFirstChatOfDay,
        )
}
