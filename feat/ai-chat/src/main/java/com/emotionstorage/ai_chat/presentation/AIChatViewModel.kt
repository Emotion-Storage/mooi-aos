package com.emotionstorage.ai_chat.presentation

import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.useCase.chat.ConnectChatRoomUseCase
import com.emotionstorage.domain.useCase.chat.DisconnectChatRoomUseCase
import com.emotionstorage.domain.useCase.chat.GetChatRoomMessagesUseCase
import com.emotionstorage.domain.useCase.chat.ObserveChatMessagesUseCase
import com.emotionstorage.domain.useCase.chat.SendChatMessageUseCase
import com.emotionstorage.domain.useCase.chat.TempSaveChatRoomUseCase
import com.emotionstorage.domain.useCase.timeCapsule.CreateTimeCapsuleUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_PROGRESS = 0.03f
private const val TIME_CAPSULE_CREATE_SCORE = 70f

data class AIChatState(
    val roomId: Long = 0L,
    val messages: List<ChatMessage> = emptyList(),
    val canCreateTimesCapsule: Boolean = false,
    val chatProgress: Float = 0.03f,
    val turnScore: Int = 0,
    val gaugeScore: Int = 0,
    val isWaitingReply: Boolean = false,
    val isCreatingTimeCapsule: Boolean = false,
    val forceQuitTriggerTurn: Int? = null,
    val hasShownForceQuitBottomSheet: Boolean = false,
    val showForceQuitBottomSheet: Boolean = false,
    val isMooiTyping: Boolean = false,
    val isLoadingHistory: Boolean = false,
)

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

    object TempSaveChatRoom : AIChatAction()
}

sealed class AIChatSideEffect : BaseSideEffect {
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
    private val createTimeCapsule: CreateTimeCapsuleUseCase,
    private val tempSaveChatRoom: TempSaveChatRoomUseCase,
    private val getChatRoomMessages: GetChatRoomMessagesUseCase,
) : BaseViewModel<AIChatState>(AIChatState()) {
    private var chatMessageObserverJob: Job? = null

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

            is AIChatAction.TempSaveChatRoom -> {
                handleTempSave()
            }
        }
    }

    private fun handleConnectChatRoom(roomId: Long) =
        baseIntent {
            // todo: delete toast test code
            postSideEffect(AIChatSideEffect.CanCreateTimesCapsule)


            // update room id
            reduce {
                state.copy(roomId = roomId)
            }
            // cancel previous message observer job, if exists
            chatMessageObserverJob?.cancel()

            // connect chat room
            connectChatRoom(roomId).handle(
                onSuccess = {
                    reduce { state.copy(isLoadingHistory = true) }

                    getChatRoomMessages(cursor = null).handle(
                        onSuccess = { data ->
                            val historyMessages: List<ChatMessage> = data

                            val gauge: Int = historyMessages.lastOrNull()?.gaugeScore ?: 0
                            val computed = (gauge / TIME_CAPSULE_CREATE_SCORE).coerceIn(0f, 1f)
                            val progress: Float = if (gauge == 0) MIN_PROGRESS else maxOf(MIN_PROGRESS, computed)
                            val canCreate: Boolean = gauge >= TIME_CAPSULE_CREATE_SCORE

                            reduce {
                                state.copy(
                                    messages = historyMessages,
                                    gaugeScore = gauge,
                                    chatProgress = progress,
                                    canCreateTimesCapsule = canCreate,
                                    isLoadingHistory = false,
                                )
                            }
                        },
                        onError = { throwable, code, data ->
                            reduce { state.copy(isLoadingHistory = false) }
                            Logger.e("history load failed: $throwable")
                        },
                    )
                    // start observing chat messages
                    launchChatMessageObserver(roomId)
                },
                onError = { throwable, code, data ->
                    Logger.e("chat room connection failed, $throwable")
                    throw BaseException(
                        message = throwable.message ?: "chat room connection failed",
                        code = code,
                        cause = throwable,
                    )
                },
            )
        }

    private fun launchChatMessageObserver(roomId: Long): Job =
        baseIntent {
            // cancel previous message observer job, if exists
            chatMessageObserverJob?.cancel()

            chatMessageObserverJob =
                baseViewModelScope.launch {
                    observeChatMessages(roomId)
                        .onEach { message ->
                            val isComplete = message.isComplete
                            val nextGauge: Int = message.gaugeScore ?: state.gaugeScore
                            val computed = (nextGauge / TIME_CAPSULE_CREATE_SCORE).coerceIn(0f, 1f)
                            val newProgress: Float =
                                if (nextGauge == 0) {
                                    state.chatProgress
                                } else {
                                    maxOf(MIN_PROGRESS, computed)
                                }
                            val canCreate: Boolean = nextGauge >= TIME_CAPSULE_CREATE_SCORE

                            if (state.isWaitingReply && isComplete) {
                                reduce {
                                    state.copy(
                                        isMooiTyping = false,
                                        isWaitingReply = false,
                                    )
                                }
                            }

                            val isNewlyCreatable = canCreate && !state.canCreateTimesCapsule
                            if (isNewlyCreatable) {
                                postSideEffect(AIChatSideEffect.CanCreateTimesCapsule)
                            }

                            reduce {
                                val rawTurnCountScore = message.turnCountScore

                                val nextTurnScore =
                                    when {
                                        !isComplete -> state.turnScore
                                        rawTurnCountScore != null && rawTurnCountScore > 0 -> rawTurnCountScore
                                        else -> state.turnScore + 1
                                    }

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
                                    gaugeScore = nextGauge,
                                    chatProgress = newProgress,
                                    canCreateTimesCapsule = canCreate,
                                    turnScore = nextTurnScore,
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
        baseIntent {
            if (message.isBlank() || state.isWaitingReply) return@baseIntent
            val outgoing =
                ChatMessage.newClientMessage(
                    roomId = state.roomId,
                    content = message,
                )
            reduce {
                state.copy(
                    messages = state.messages + outgoing,
                    isWaitingReply = true,
                    isMooiTyping = true,
                )
            }

            sendChatMessage(state.roomId, outgoing).handle(
                onSuccess = {
                    Logger.i("chat message sent")
                },
                onError = { throwable, code, data ->
                    Logger.e("chat message sending failed, $throwable")
                    reduce {
                        state.copy(
                            messages = state.messages.filterNot { it.clientId == outgoing.clientId },
                            isWaitingReply = false,
                            isMooiTyping = false,
                        )
                    }
                    throw BaseException(
                        message = throwable.message ?: "chat message sending failed",
                        code = code,
                        cause = throwable,
                    )
                },
            )
            // updateChatProgress()
        }

    private fun handleExitChatRoom() =
        baseIntent {
            // cancel current message observer job
            chatMessageObserverJob?.cancel()

            disconnectChatRoom(state.roomId).handle(
                onSuccess = {
                    Logger.d("chat room disconnected")
                },
                onError = { throwable, code, data ->
                    Logger.e("chat room disconnection failed, $throwable")
                    throw BaseException(
                        message = throwable.message ?: "chat room disconnection failed",
                        code = code,
                        cause = throwable,
                    )
                },
            )
        }

    private fun handleCreateTimeCapsule() =
        baseIntent {
            if (!state.canCreateTimesCapsule) {
                Logger.w("Can't create time capsule yet")
                return@baseIntent
            }
            reduce { state.copy(isCreatingTimeCapsule = true) }

            try {
                createTimeCapsule(state.roomId).handle(
                    onSuccess = { capsuleId ->
                        handleExitChatRoom()
                        postSideEffect(
                            AIChatSideEffect.CreateTimeCapsuleSuccess(capsuleId),
                        )
                    },
                    onError = { throwable, code, data ->
                        Logger.e("createTimeCapsule error: $throwable")

                        throw BaseException(
                            message = throwable.message ?: "createTimeCapsule error",
                            code = code,
                            cause = throwable,
                        )
                    },
                )
            } finally {
                reduce { state.copy(isCreatingTimeCapsule = false) }
            }
        }

    private fun handleForceQuitSheet() =
        baseIntent {
            reduce {
                state.copy(showForceQuitBottomSheet = false)
            }
        }

    private fun handleTempSave() =
        baseIntent {
            chatMessageObserverJob?.cancel()

            tempSaveChatRoom(state.roomId).handle(
                onSuccess = {
                    postSideEffect(AIChatSideEffect.NavigateBack)
                },
                onError = { throwable, code, data ->
                    Logger.e("temp save error")
                    // todo: 임시저장 실패 시, 처리 고민 (채팅 이어서 재시작 / 그냥 나가기)
                    throw BaseException(
                        message = throwable.message ?: "temp save error",
                        code = code,
                        cause = throwable,
                    )
                },
            )
        }
}
