package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.ChatWSDataSource
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.sendText
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.WebSocketClient
import org.hildan.krossbow.websocket.builtin.builtIn
import javax.inject.Inject

// todo: change to local property
private val url = "ws://__:8080//ws"

class ChatWSDataSourceImpl @Inject constructor() : ChatWSDataSource {
    val client = StompClient(WebSocketClient.builtIn()) // other config can be passed in here
    lateinit var session: StompSession

    override suspend fun connectChatRoom(): Boolean {
        try {
            client.connect(url)
            return true
        }catch (e: Exception){
            throw Throwable("connectChatRoom() failed", e)
        }
    }

    override suspend fun disconnectChatRoom(): Boolean {
        try {
            session.disconnect()
            return true
        } catch (e: Exception) {
            throw Throwable("disconnectChatRoom() failed", e)
        }
    }

    override suspend fun observeChatMessages(roomId: String): Flow<ChatMessage> {
        val subscription: Flow<String> = session.subscribeText("/sub/chatroom/${roomId}")
        return subscription.map {
            ChatMessage(
                roomId = roomId,
                source = ChatMessage.MessageSource.SERVER,
                content = it
            )
        }
    }

    override suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean {
        try {
            // todo: send json
            session.sendText(destination = "/pub/v1/test", body = "chat message")
            return true
        } catch (e: Exception) {
            throw Throwable("sendChatMessage() failed", e)
        }
    }
}