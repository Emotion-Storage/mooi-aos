package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.useCase.chat.ConnectChatRoomUseCase
import com.emotionstorage.domain.useCase.chat.DisconnectChatRoomUseCase
import com.emotionstorage.domain.useCase.chat.ObserveChatMessagesUseCase
import com.emotionstorage.domain.useCase.chat.SaveTempSaveRoomIdUseCase
import com.emotionstorage.domain.useCase.chat.SendChatMessageUseCase
import com.emotionstorage.domain.useCase.chat.TempSaveChatRoomUseCase
import com.emotionstorage.domain.useCase.timeCapsule.CreateTimeCapsuleUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

private const val TIME_CAPSULE_CREATE_SCORE = 70

data class AIChatState(
    val roomId: Long = 0L,
    val messages: List<ChatMessage> = emptyList(),
    val canCreateTimesCapsule: Boolean = false,
    val chatProgress: Float = 0.03f,
    val turnScore: Int = 0,
    val isWaitingReply: Boolean = false,
    val isCreatingTimeCapsule: Boolean = false,
    val forceQuitTriggerTurn: Int? = null,
    val hasShownForceQuitBottomSheet: Boolean = false,
    val showForceQuitBottomSheet: Boolean = false,
    val isMooiTyping: Boolean = false,
) {
    val isEmpty: Boolean get() = messages.isEmpty()
    val hasTodayHistory: Boolean
        get() = messages.any { it.timestamp.toLocalDate() == LocalDate.now() }
    val isNewDayFirstChat: Boolean
        get() = !isEmpty && !hasTodayHistory
}

sealed class AIChatAction {
    data class ConnectChatRoom(
        val roomId: Long,
    ) : AIChatAction()

    data class SendChatMessage(
        val message: String,
    ) : AIChatAction()

    object ExitChatRoom : AIChatAction()

    object CreateTimeCapsule : AIChatAction()

    object DismissForceQuitSheet : AIChatAction()

    object TempSaveAndExitChatRoom : AIChatAction()
}

sealed class AIChatSideEffect {
    data class ToastMessage(
        val message: String,
    ) : AIChatSideEffect()

    object CanCreateTimesCapsule : AIChatSideEffect()

    data class CreateTimeCapsuleSuccess(
        val capsuleId: Long,
    ) : AIChatSideEffect()

    object NavigateBack : AIChatSideEffect()
}

@HiltViewModel
class AIChatViewModel @Inject constructor(
    private val connectChatRoom: ConnectChatRoomUseCase,
    private val disconnectChatRoom: DisconnectChatRoomUseCase,
    private val sendChatMessage: SendChatMessageUseCase,
    private val observeChatMessages: ObserveChatMessagesUseCase,
    private val createTimeCapsuleUseCase: CreateTimeCapsuleUseCase,
    private val tempSaveChatRoomUseCase: TempSaveChatRoomUseCase,
    private val saveTempSaveRoomIdUseCase: SaveTempSaveRoomIdUseCase,
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

            is AIChatAction.DismissForceQuitSheet -> {
                handleForceQuitSheet()
            }

            is AIChatAction.TempSaveAndExitChatRoom -> {
                handleTempSaveAndExit()
            }
        }
    }

    private fun handleConnectChatRoom(roomId: Long) =
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

    private fun launchChatMessageObserver(roomId: Long): Job =
        intent {
            // cancel previous message observer job, if exists
            chatMessageObserverJob?.cancel()

            chatMessageObserverJob =
                viewModelScope.launch {
                    observeChatMessages(roomId)
                        .onEach { message ->
                            val isComplete = message.isComplete
                            val gaugeScore = message.gaugeScore
                            val newProgress =
                                gaugeScore
                                    ?.let { score -> (score / 70f).coerceIn(0f, 1f) }
                                    ?: state.chatProgress

                            val canCreate =
                                gaugeScore?.let { it >= TIME_CAPSULE_CREATE_SCORE }
                                    ?: state.canCreateTimesCapsule

                            if (state.isWaitingReply && isComplete) {
                                reduce {
                                    state.copy(
                                        isMooiTyping = false,
                                        isWaitingReply = false,
                                    )
                                }
                            }

                            // TODO : gauge 값이 간헐적으로 null이 안들어오게 된다면 !! turnScore 값을 non-null type으로 변경
                            reduce {
                                val rawTurnCountScore = message.turnCountScore

                                val nextTurnScore =
                                    when {
                                        !isComplete -> state.turnScore
                                        rawTurnCountScore != null && rawTurnCountScore > 0 -> rawTurnCountScore
                                        else -> state.turnScore + 1
                                    }

                                val isNewlyCreatable = canCreate && !state.canCreateTimesCapsule
                                val quitTriggerTurn =
                                    when {
                                        state.forceQuitTriggerTurn != null -> state.forceQuitTriggerTurn
                                        isNewlyCreatable -> nextTurnScore + 10
                                        else -> null
                                    }

                                val shouldShowForceQuit =
                                    !state.hasShownForceQuitBottomSheet &&
                                        quitTriggerTurn != null &&
                                        nextTurnScore >= quitTriggerTurn

                                state.copy(
                                    messages =
                                        if (isComplete) state.messages else state.messages + message,
                                    chatProgress = newProgress,
                                    canCreateTimesCapsule = canCreate,
                                    turnScore = nextTurnScore!!,
                                    forceQuitTriggerTurn = quitTriggerTurn,
                                    hasShownForceQuitBottomSheet =
                                        state.hasShownForceQuitBottomSheet ||
                                            shouldShowForceQuit,
                                    showForceQuitBottomSheet = shouldShowForceQuit,
                                )
                            }
                        }.catch {
                            intent {
                                reduce {
                                    state.copy(isWaitingReply = false, isMooiTyping = false)
                                }
                            }
                        }.onCompletion {
                            intent {
                                reduce {
                                    state.copy(isWaitingReply = false, isMooiTyping = false)
                                }
                            }
                        }.collect()
                }
        }

    private fun handleSendMessage(message: String) =
        intent {
            if (message.isBlank() || state.isWaitingReply) return@intent
            val newMessage =
                ChatMessage(
                    roomId = state.roomId,
                    source = ChatMessage.MessageSource.CLIENT,
                    content = message,
                )

            reduce {
                state.copy(
                    messages = state.messages + newMessage,
                    isWaitingReply = true,
                    isMooiTyping = true,
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
                        Logger.d("chat room disconnected + $result")
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

            val roomId = state.roomId
            if (roomId == 0L) {
                Logger.e("Invalid roomId: $roomId")
                postSideEffect(AIChatSideEffect.ToastMessage("채팅방 정보가 올바르지 않아요"))
                return@intent
            }

            reduce { state.copy(isCreatingTimeCapsule = true) }

            try {
                when (val result = createTimeCapsuleUseCase(roomId)) {
                    is DataState.Success -> {
                        val capsuleId = result.data

                        handleExitChatRoom()
                        postSideEffect(
                            AIChatSideEffect.CreateTimeCapsuleSuccess(capsuleId),
                        )
                    }

                    is DataState.Error -> {
                        Logger.e("createTimeCapsule error: ${result.throwable}")
                        postSideEffect(
                            AIChatSideEffect.ToastMessage("타임캡슐 생성 실패"),
                        )
                    }

                    is DataState.Loading -> {
                        // no - op
                    }
                }
            } finally {
                reduce { state.copy(isCreatingTimeCapsule = false) }
            }
        }

    private fun handleForceQuitSheet() =
        intent {
            reduce {
                state.copy(showForceQuitBottomSheet = false)
            }
        }

    private fun handleTempSaveAndExit() =
        intent {
            chatMessageObserverJob?.cancel()

            val roomId = state.roomId
            // TODO : ToastMesage 추후 제거
            if (roomId == 0L) {
                postSideEffect(AIChatSideEffect.ToastMessage("채팅방 정보가 올바르지 않아요"))
                postSideEffect(AIChatSideEffect.NavigateBack)
                return@intent
            }

            when (val save = tempSaveChatRoomUseCase(roomId)) {
                is DataState.Success -> {
                    saveTempSaveRoomIdUseCase(save.data)
                    postSideEffect(AIChatSideEffect.ToastMessage("임시 저장 완료"))
                }

                is DataState.Error -> {
                    postSideEffect(AIChatSideEffect.ToastMessage("임시 저장 실패"))
                }

                else -> {
                    Unit
                }
            }

            disconnectChatRoom(roomId).collect { result ->
                when (result) {
                    is DataState.Success -> postSideEffect(AIChatSideEffect.ToastMessage("채팅방 나가기 성공"))
                    is DataState.Error -> postSideEffect(AIChatSideEffect.ToastMessage("채팅방 나가기 실패"))
                    is DataState.Loading -> Unit
                }
            }

            postSideEffect(AIChatSideEffect.NavigateBack)
        }
}
