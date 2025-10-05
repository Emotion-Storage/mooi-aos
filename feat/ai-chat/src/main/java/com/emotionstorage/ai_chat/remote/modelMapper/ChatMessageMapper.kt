package com.emotionstorage.ai_chat.remote.modelMapper

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.ai_chat.remote.response.ChatMessageRequestBody

object ChatMessageMapper {
    fun toRemote(chatMessage: ChatMessage): ChatMessageRequestBody =
        ChatMessageRequestBody(
            messageId = chatMessage.id,
            roomId = chatMessage.roomId,
            content = chatMessage.content,
        )
}
