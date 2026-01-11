package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.ChatRoomMessagesEntity
import com.emotionstorage.domain.model.ChatMessage
import java.time.LocalDateTime
import java.time.OffsetDateTime

object ChatMessageMapper {
    fun toDomainMessages(entity: ChatRoomMessagesEntity): List<ChatMessage> {
        val roomId = entity.roomWithChats.chatRoomId

        return entity
            .roomWithChats
            .chats
            .sortedBy { it.id }
            .map { chat ->
                ChatMessage(
                    id = chat.id.toString(),
                    clientId = chat.clientId,
                    roomId = roomId,
                    source = toMessageSource(chat.sender),
                    content = chat.message,
                    gaugeScore = entity.roomWithChats.gauge,
                    turnCountScore = null,
                    isComplete = true,
                    timestamp = parseToLocalDateTime(chat.chatTime),
                )
            }
    }

    private fun toMessageSource(sender: String): ChatMessage.MessageSource =
        when (sender.uppercase()) {
            "USER" -> ChatMessage.MessageSource.CLIENT
            "MOOI" -> ChatMessage.MessageSource.SERVER
            else -> ChatMessage.MessageSource.SERVER
        }

    private fun parseToLocalDateTime(raw: String): LocalDateTime =
        runCatching { OffsetDateTime.parse(raw).toLocalDateTime() }
            .getOrElse { LocalDateTime.parse(raw) }
}
