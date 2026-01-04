package com.emotionstorage.remote.modelMapper

import com.emotionstorage.data.model.ChatMessageEntity
import com.emotionstorage.data.model.ChatRoomMessagesEntity
import com.emotionstorage.data.model.RoomWithChatsEntity
import com.emotionstorage.remote.response.chat.ChatRoomMessagesResponse

object ChatRoomMessagesMapper {
    fun toData(response: ChatRoomMessagesResponse): ChatRoomMessagesEntity =
        ChatRoomMessagesEntity(
            roomWithChats =
                RoomWithChatsEntity(
                    chatRoomId = response.roomWithChats.chatRoomId,
                    firstChatTime = response.roomWithChats.firstChatTime,
                    totalChatCount = response.roomWithChats.totalChatCount,
                    chats =
                        response.roomWithChats.chats.map { chat ->
                            ChatMessageEntity(
                                id = chat.id,
                                sender = chat.sender,
                                message = chat.message,
                                chatTime = chat.chatTime,
                            )
                        },
                ),
            nextCursor = response.nextCursor,
            hasNext = response.hasNext,
        )
}
