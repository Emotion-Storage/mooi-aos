package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.EmotionChatSessionEntity
import com.emotionstorage.remote.response.chat.EmotionChatSessionResponse

object EmotionChatSessionMapper {
    fun toData(response: EmotionChatSessionResponse): EmotionChatSessionEntity =
        EmotionChatSessionEntity(
            roomId = response.roomId,
            isTempSave = response.isTempSave,
            isFirstChatOfDay = response.isFirstChatOfDay,
        )
}
