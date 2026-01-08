package com.emotionstorage.data.model

data class ChatRoomMessagesEntity(
    val roomWithChats: RoomWithChatsEntity,
    val nextCursor: Long?,
    val hasNext: Boolean,
)

data class RoomWithChatsEntity(
    val chatRoomId: Long,
    val firstChatTime: String,
    val totalChatCount: Int,
    val gauge: Int,
    val chats: List<ChatMessageEntity>,
)

data class ChatMessageEntity(
    val id: Long,
    val sender: String,
    val message: String,
    val chatTime: String,
)
