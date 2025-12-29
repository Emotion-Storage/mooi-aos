package com.emotionstorage.ai_chat.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomMessagesResponse(
    val roomWithChats: RoomWithChatsResponse,
    val nextCursor: Long?,
    val hasNext: Boolean,
)

@Serializable
data class RoomWithChatsResponse(
    val chatRoomId: Long,
    val firstChatTime: String,
    val totalChatCount: Int,
    val chats: List<ChatMessageResponse>,
)

@Serializable
data class ChatMessageResponse(
    val id: Long,
    val sender: String,
    val message: String,
    val chatTime: String,
)


