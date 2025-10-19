package com.emotionstorage.ai_chat.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequestBody(
    val messageId: String,
    val roomId: Long,
    val content: String,
    val messageType: String,
    val timestamp: String
)
