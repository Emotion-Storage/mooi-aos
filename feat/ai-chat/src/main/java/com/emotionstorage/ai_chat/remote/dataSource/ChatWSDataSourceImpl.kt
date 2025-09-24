package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.ChatWSDataSource
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.remote.modelMapper.ChatMessageMapper
import com.emotionstorage.ai_chat.remote.response.ChatMessageRequestBody
import com.emotionstorage.remote.BuildConfig
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.subscribeText
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import javax.inject.Inject

private const val WS_URL = "ws://${BuildConfig.MOOI_DEV_SERVER_URL}ws"

class ChatWSDataSourceImpl
    @Inject
    constructor() : ChatWSDataSource {
        private val json = Json { ignoreUnknownKeys = true }
        private val client =
            StompClient(
                webSocketClient =
                    OkHttpWebSocketClient(
                        OkHttpClient.Builder()
                            .pingInterval(Duration.ofSeconds(10))
                            .build(),
                    ),
            )

        private lateinit var session: StompSession

        override suspend fun connectChatRoom(): Boolean {
            try {
                session = client.connect(WS_URL)
                return true
            } catch (e: Exception) {
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

        override suspend fun observeChatMessages(roomId: String): Flow<String> {
            return session.subscribeText("/sub/chatroom/$roomId").map {
                Logger.d("observeChatMessages() it: $it")
                it
            }
        }

        override suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean {
            try {
                val messageJson =
                    json.encodeToString(
                        ChatMessageRequestBody.serializer(),
                        ChatMessageMapper.toRemote(chatMessage),
                    )
                Logger.d("sendChatMessage() messageJson: $messageJson")

                session.send(
                    headers =
                        StompSendHeaders(
                            destination = "/pub/v1/test",
                        ),
                    body = FrameBody.Text(messageJson),
                )
                return true
            } catch (e: Exception) {
                throw Throwable("sendChatMessage() failed", e)
            }
        }
    }
