package com.emotionstorage.remote.response.chat

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
    val gauge: Int,
    val chats: List<ChatMessagesResponse>,
)

@Serializable
data class ChatMessagesResponse(
    val id: Long,
    val sender: String,
    val message: String,
    val chatTime: String,
)
