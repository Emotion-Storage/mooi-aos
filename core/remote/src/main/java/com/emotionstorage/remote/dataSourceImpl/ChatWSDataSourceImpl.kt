package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ChatWSDataSource
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.useCase.auth.GetAccessTokenUseCase
import com.emotionstorage.remote.BuildConfig
import com.emotionstorage.remote.modelMapper.ChatMessageMapper
import com.emotionstorage.remote.request.chat.ChatMessageRequestBody
import com.emotionstorage.remote.response.chat.ChatMessageResponse
import com.orhanobut.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
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
    private val json: Json,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ChatWSDataSource {

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
                flow {
                    raw
                        .lines()
                        // 응답에 문제가 없다면 필요 없는 부분
                        .map { it.replace("\uFFFD", "").trim() }
                        .filter { it.isNotBlank() }
                        .forEach { line ->
                            Logger.d("observeChatMessages() line: $line")
                            try {
                                val dto = json.decodeFromString<ChatMessageResponse>(line)
                                val isComplete = dto.messageType == "chat.complete"

                                val content = dto.content.orEmpty()
                                if (content.isBlank() && !isComplete) return@forEach

                                if (!isComplete) {
                                    delay(1500L)
                                }

                                emit(
                                    ChatMessage(
                                        roomId = roomId,
                                        source = ChatMessage.MessageSource.SERVER,
                                        content = content,
                                        gaugeScore = dto.gauge?.gaugeScore,
                                        turnCountScore = dto.gauge?.turnCountScore,
                                        isComplete = isComplete,
                                    ),
                                )
                            } catch (e: Exception) {
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
