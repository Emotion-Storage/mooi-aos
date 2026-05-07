package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ChatWebSocketDataSource
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
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.stomp.StompSession
import org.hildan.krossbow.stomp.frame.FrameBody
import org.hildan.krossbow.stomp.headers.StompSendHeaders
import org.hildan.krossbow.stomp.subscribeText
import java.util.UUID
import javax.inject.Inject

class ChatWebSocketSDataSourceImpl @Inject constructor(
    private val json: Json,
    private val client: StompClient,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ChatWebSocketDataSource {
    private val wsUrl =
        "wss://${if (BuildConfig.DEBUG) BuildConfig.MOOI_DEV_SERVER_URL else BuildConfig.MOOI_PROD_SERVER_URL}/ws"
    private var session: StompSession? = null

    override suspend fun connectChatRoom(): Boolean {
        try {
            val token = getAccessTokenUseCase() ?: throw IllegalStateException("토큰이 없어 연결이 불가능합니다.")
            val connectHeaders = mapOf("Authorization" to "Bearer $token")

            session = client.connect(url = wsUrl, customStompConnectHeaders = connectHeaders)
            return true
        } catch (e: Exception) {
            throw Throwable("connectChatRoom() failed", e)
        }
    }

    override suspend fun disconnectChatRoom(): Boolean {
        try {
            session?.disconnect()
            return true
        } catch (e: Exception) {
            throw Throwable("disconnectChatRoom() failed", e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun observeChatMessages(roomId: Long): Flow<ChatMessage> =
        session
            ?.subscribeText("/sub/chatroom/$roomId")
            ?.flatMapConcat { raw ->
                flow {
                    raw
                        .lines()
                        .filter { it.isNotBlank() }
                        .forEach { line ->
                            Logger.d("observeChatMessages() line: $line")
                            try {
                                val dto = json.decodeFromString<ChatMessageResponse>(line)
                                val isComplete = dto.messageType == "chat.complete"
                                val content = dto.content.orEmpty()

                                if (content.isBlank() && !isComplete) return@forEach
                                if (!isComplete) {
                                    delay(800L)
                                }

                                // Server에서 내려오는 값이 없어 UUID로 식별
                                val chunkClientId = UUID.randomUUID().toString()

                                emit(
                                    ChatMessage.newServerMessage(
                                        roomId = roomId,
                                        clientId = chunkClientId,
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
            } ?: emptyFlow()

    override suspend fun sendChatMessage(chatMessage: ChatMessage): Boolean {
        try {
            val token = getAccessTokenUseCase() ?: throw IllegalStateException("토큰이 없어 메세지 전송이 불가능 합니다.")

            val messageJson =
                json.encodeToString(
                    ChatMessageRequestBody.serializer(),
                    ChatMessageMapper.toRemote(chatMessage),
                )
            Logger.d("sendChatMessage() messageJson: $messageJson")

            if (session == null) {
                throw IllegalStateException("채팅 세션이 없어 메세지 전송이 불가능 합니다.")
            } else {
                session!!.send(
                    headers =
                        StompSendHeaders(
                            destination = "/pub/v1/chat",
                            customHeaders = mapOf("Authorization" to "Bearer $token"),
                        ),
                    body = FrameBody.Text(messageJson),
                )
            }
            return true
        } catch (e: Exception) {
            throw Throwable(e.message ?: "sendChatMessage() failed", e)
        }
    }
}
