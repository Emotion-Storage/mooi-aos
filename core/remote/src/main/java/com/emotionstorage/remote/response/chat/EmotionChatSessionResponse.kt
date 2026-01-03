package com.emotionstorage.remote.response.chat

import kotlinx.serialization.Serializable

@Serializable
data class EmotionChatSessionResponse(
    val roomId: Long,
    val isTempSave: Boolean,
    val isFirstChatOfDay: Boolean,
)
