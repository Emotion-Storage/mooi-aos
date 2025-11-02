package com.emotionstorage.ai_chat.remote.modelMapper

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.ai_chat.remote.response.ChatMessageRequestBody
import java.time.format.DateTimeFormatter

object ChatMessageMapper {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun toRemote(chatMessage: ChatMessage): ChatMessageRequestBody =
        ChatMessageRequestBody(
            messageId = chatMessage.id,
            roomId = chatMessage.roomId,
            content = chatMessage.content,
            messageType = "USER",
            timestamp = chatMessage.timestamp.format(formatter),
        )
}
