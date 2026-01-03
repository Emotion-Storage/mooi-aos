package com.emotionstorage.remote.response.chat

import com.emotionstorage.data.model.StartEmotionConversationEntity
import kotlinx.serialization.Serializable

@Serializable
data class StartEmotionConversationResponse(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)

fun StartEmotionConversationResponse.toEntity(): StartEmotionConversationEntity =
    StartEmotionConversationEntity(
        roomId = roomId,
        isTempSave = isTempSave,
        isFirstChatOfDay = isFirstChatOfDay,
    )
