package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.usecase.remote.ConnectChatRoomUseCase
import com.emotionstorage.ai_chat.domain.usecase.remote.DisconnectChatRoomUseCase
import com.emotionstorage.ai_chat.domain.usecase.remote.ObserveChatMessagesUseCase
import com.emotionstorage.ai_chat.domain.usecase.remote.SendChatMessageUseCase
import com.emotionstorage.domain.common.DataState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class AIChatState(
    val roomId: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val canCreateTimesCapsule: Boolean = false,
    val chatProgress: Float = 0f,
)

sealed class AIChatAction {
    data class ConnectChatRoom(
        val roomId: String,
    ) : AIChatAction()

    data class SendChatMessage(
        val message: String,
    ) : AIChatAction()

    object ExitChatRoom : AIChatAction()

    object CreateTimeCapsule : AIChatAction()
}

sealed class AIChatSideEffect {
    data class ToastMessage(
        val message: String,
    ) : AIChatSideEffect()

    object CanCreateTimesCapsule : AIChatSideEffect()

    data class CreateTimeCapsuleSuccess(
        val capsuleId: String,
    ) : AIChatSideEffect()
}

@HiltViewModel
class AIChatViewModel
    @Inject
    constructor(
        private val connectChatRoom: ConnectChatRoomUseCase,
        private val disconnectChatRoom: DisconnectChatRoomUseCase,
        private val sendChatMessage: SendChatMessageUseCase,
        private val observeChatMessages: ObserveChatMessagesUseCase,
    ) : ViewModel(),
        ContainerHost<AIChatState, AIChatSideEffect> {
        private var chatMessageObserverJob: Job? = null

        override val container = container<AIChatState, AIChatSideEffect>(AIChatState())

        fun onAction(action: AIChatAction) {
            when (action) {
                is AIChatAction.ConnectChatRoom -> {
                    handleConnectChatRoom(action.roomId)
                }

                is AIChatAction.SendChatMessage -> {
                    handleSendMessage(action.message)
                }

                is AIChatAction.ExitChatRoom -> {
                    handleExitChatRoom()
                }

                is AIChatAction.CreateTimeCapsule -> {
                    handleCreateTimeCapsule()
                }
            }
        }

        private fun handleConnectChatRoom(roomId: String) =
            intent {
                // update room id
                reduce {
                    state.copy(roomId = roomId)
                }
                // cancel previous message observer job, if exists
                chatMessageObserverJob?.cancel()

                // connect chat room
                connectChatRoom(roomId).collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            Logger.i("chat room connected")
                            postSideEffect(AIChatSideEffect.ToastMessage("채팅방 연결 성공"))

                            // start observing chat messages
                            launchChatMessageObserver(roomId)
                        }

                        is DataState.Error -> {
                            Logger.e("chat room connection failed, ${result.throwable}")
                            postSideEffect(AIChatSideEffect.ToastMessage("채팅방 연결 실패"))
                        }

                        is DataState.Loading -> {
                            Logger.d("chat room connection loading...")
                        }
                    }
                }
            }

        private fun launchChatMessageObserver(roomId: String): Job =
            intent {
                // cancel previous message observer job, if exists
                chatMessageObserverJob?.cancel()

                chatMessageObserverJob =
                    viewModelScope.launch {
                        observeChatMessages(roomId).collect { message ->
                            reduce {
                                state.copy(
                                    messages = state.messages + message,
                                )
                            }
                        }
                    }
            }

        private fun handleSendMessage(message: String) =
            intent {
                val newMessage =
                    ChatMessage(
                        roomId = state.roomId,
                        source = ChatMessage.MessageSource.CLIENT,
                        content = message,
                    )
                // optimistic state update
                reduce {
                    state.copy(
                        messages = state.messages + newMessage,
                    )
                }

                sendChatMessage(
                    state.roomId,
                    ChatMessage(
                        roomId = state.roomId,
                        source = ChatMessage.MessageSource.CLIENT,
                        content = message,
                    ),
                ).collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            Logger.i("chat message sent")
                        }

                        is DataState.Error -> {
                            Logger.e("chat message sending failed, ${result.throwable}")
                            postSideEffect(AIChatSideEffect.ToastMessage("메세지 전송 실패"))

                            reduce {
                                state.copy(
                                    messages = state.messages.filterNot { it == newMessage },
                                )
                            }
                        }

                        is DataState.Loading -> {
                            Logger.d("chat message sending loading...")
                        }
                    }
                }
                // updateChatProgress()
            }

        private fun handleExitChatRoom() =
            intent {
                // cancel current message observer job
                chatMessageObserverJob?.cancel()

                disconnectChatRoom(state.roomId).collect { result ->
                    when (result) {
                        is DataState.Success -> {
                            Logger.i("chat room disconnected")
                            postSideEffect(AIChatSideEffect.ToastMessage("채팅방 나가기 성공"))
                        }

                        is DataState.Error -> {
                            Logger.e("chat room disconnection failed, ${result.throwable}")
                            postSideEffect(AIChatSideEffect.ToastMessage("채팅방 나가기 실패"))
                        }

                        is DataState.Loading -> {
                            Logger.d("chat room disconnection loading...")
                        }
                    }
                }
            }

        private fun handleCreateTimeCapsule() =
            intent {
                if (!state.canCreateTimesCapsule) {
                    Logger.w("Can't create time capsule yet")
                    return@intent
                }

                // disconnect chat room
                handleExitChatRoom()

                // todo: get time capsule id from server
                postSideEffect(AIChatSideEffect.CreateTimeCapsuleSuccess("123"))
            }

        // TODO : 메세지 진행률 업데이트 관련 로직
        private fun updateChatProgress() =
            intent {
                reduce {
                    state.copy()
                }
            }
    }
