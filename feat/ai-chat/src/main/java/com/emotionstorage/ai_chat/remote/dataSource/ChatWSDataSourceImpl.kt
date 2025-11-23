package com.emotionstorage.ai_chat.remote.dataSource

import com.emotionstorage.ai_chat.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.ai_chat.remote.modelMapper.ChatMessageMapper
import com.emotionstorage.ai_chat.remote.response.ChatMessageRequestBody
import com.emotionstorage.ai_chat.remote.response.ChatMessageResponse
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.useCase.auth.GetAccessTokenUseCase
import com.emotionstorage.remote.BuildConfig
import com.orhanobut.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
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

class ChatWSDataSourceImpl @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ChatWSDataSource {
    private val json = Json { ignoreUnknownKeys = true }

    private val client =
        StompClient(
            webSocketClient =
                OkHttpWebSocketClient(
                    OkHttpClient
                        .Builder()
                        .pingInterval(Duration.ofSeconds(10))
                        .build(),
                ),
        )

    private lateinit var session: StompSession

    override suspend fun connectChatRoom(): Boolean {
        try {
            val token = getAccessTokenUseCase() ?: throw IllegalStateException("토큰이 없어 연결이 불가능합니다.")
            val connectHeaders = mapOf("Authorization" to "Bearer $token")

            session = client.connect(url = WS_URL, customStompConnectHeaders = connectHeaders)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage> =
        session
            .subscribeText("/sub/chatroom/$roomId")
            .flatMapConcat { raw ->
                kotlinx.coroutines.flow.flow {
                    raw
                        .lines()
                        .map { it.replace("\uFFFD", "").trim() }
                        .filter { it.isNotBlank() }
                        .forEach { line ->
                            Logger.d("observeChatMessages() line: $line")
                            runCatching {
                                val dto = json.decodeFromString<ChatMessageResponse>(line)

                                val isComplete = dto.messageType == "chat.complete"

                                if (!isComplete) {
                                    kotlinx.coroutines.delay(1500L)
                                }

                                emit(
                                    ChatMessage(
                                        roomId = roomId,
                                        source = ChatMessage.MessageSource.SERVER,
                                        content = dto.content.orEmpty(),
                                        gaugeScore = dto.gauge?.gaugeScore,
                                        turnCountScore = dto.gauge?.turnCountScore ?: 0,
                                        isComplete = isComplete,
                                    ),
                                )
                            }.onFailure { e ->
                                Logger.e("observeChatMessages() decode failed. line=$line", e)
                            }
                        }
                }
            }

    override suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean {
        try {
            val token = getAccessTokenUseCase() ?: throw IllegalStateException("토큰이 없어 메세지 전송이 불가능 합니다.")
            val messageJson =
                json.encodeToString(
                    ChatMessageRequestBody.serializer(),
                    ChatMessageMapper.toRemote(chatMessage),
                )
            Logger.d("sendChatMessage() messageJson: $messageJson")
            session.send(
                headers =
                    StompSendHeaders(
                        destination = "/pub/v1/chat",
                        customHeaders = mapOf("Authorization" to "Bearer $token"),
                    ),
                body = FrameBody.Text(messageJson),
            )
            return true
        } catch (e: Exception) {
            throw Throwable("sendChatMessage() failed", e)
        }
    }
}
