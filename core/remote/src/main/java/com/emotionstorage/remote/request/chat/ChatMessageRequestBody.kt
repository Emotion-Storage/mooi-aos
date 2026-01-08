package com.emotionstorage.remote.request.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageRequestBody(
    val messageId: String,
    val roomId: Long,
    val content: String,
    val messageType: String,
    val timestamp: String,
)
