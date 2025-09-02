package com.emotionstorage.ai_chat.domain

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage (
    val id: String = UUID.randomUUID().toString(),
    val roomId: String,
    val source: MessageSource,
    val content: String,
    val timestamp: LocalDateTime,
){
    enum class MessageSource{
        CLIENT,
        SERVER,
    }
}