package com.emotionstorage.ai_chat.remote.response

import com.emotionstorage.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ChatMessageRequestBody (
    val messageId: String,
    val roomId: String,
    val content: String,
    val messageType: String = "user_message",
    @Serializable(with = LocalDateTimeSerializer::class)
    val timeStamp: LocalDateTime = LocalDateTime.now()
)