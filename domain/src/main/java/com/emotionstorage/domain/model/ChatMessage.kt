package com.emotionstorage.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val clientId: String,
    val roomId: Long,
    val source: MessageSource,
    val content: String,
    val gaugeScore: Int? = null,
    val turnCountScore: Int? = null,
    val isComplete: Boolean = false,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    enum class MessageSource {
        CLIENT,
        SERVER,
    }

    companion object {
        fun newClientMessage(
            roomId: Long,
            content: String,
            clientId: String = UUID.randomUUID().toString(),
        ): ChatMessage =
            ChatMessage(
                clientId = clientId,
                roomId = roomId,
                source = MessageSource.CLIENT,
                content = content,
            )

        fun newServerMessage(
            roomId: Long,
            clientId: String,
            content: String,
            gaugeScore: Int? = null,
            turnCountScore: Int? = null,
            isComplete: Boolean = false,
            timestamp: LocalDateTime = LocalDateTime.now(),
        ): ChatMessage =
            ChatMessage(
                clientId = clientId,
                roomId = roomId,
                source = MessageSource.SERVER,
                content = content,
                gaugeScore = gaugeScore,
                turnCountScore = turnCountScore,
                isComplete = isComplete,
                timestamp = timestamp,
            )
    }
}
