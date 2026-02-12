package com.emotionstorage.home.presentation

import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.ChatEntry
import com.emotionstorage.domain.useCase.chat.DeleteChatRoomUseCase
import com.emotionstorage.domain.useCase.chat.GetChatRoomSessionUseCase
import com.emotionstorage.domain.useCase.home.GetHomeUseCase
import com.emotionstorage.domain.useCase.user.GetUserNicknameUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.annotation.OrbitExperimental
import javax.inject.Inject

data class HomeState(
    val isLoading: Boolean = true,
    val nickname: String = "",
    val keyCount: Int? = null,
    // chat & ticket info
    val roomId: Long? = null,
    val isChatTempSaved: Boolean = false,
    val ticketCount: Int = 0,
    val ticketLimit: Int = 10,
    // home conditional icon info
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
    val newReportId: Long? = null,
)

sealed class HomeAction {
    object Initiate : HomeAction()

    object EnterChat : HomeAction()

    object DeleteChat : HomeAction()
}

sealed class HomeSideEffect : BaseSideEffect {
    object TicketNotEnough : HomeSideEffect()

    data class EnterChatRoom(
        val entry: ChatEntry,
    ) : HomeSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val getUserNickname: GetUserNicknameUseCase,
    private val getHome: GetHomeUseCase,
    private val getChatRoomSession: GetChatRoomSessionUseCase,
    private val deleteChatRoom: DeleteChatRoomUseCase,
) : BaseViewModel<HomeState>(
    HomeState(),
) {
    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Initiate -> {
                handleInitiate()
            }

            is HomeAction.EnterChat -> {
                handleEnterChat()
            }

            is HomeAction.DeleteChat -> {
                handleDeletePendingChat()
            }
        }
    }

    private fun handleInitiate() =
        baseIntent {
            reduce {
                state.copy(isLoading = true)
            }

            val nicknameJob =
                baseViewModelScope.launch {
                    initNickname()
                }
            val homeStateJob =
                baseViewModelScope.launch {
                    initHomeState()
                }
            val chatStateJob =
                baseViewModelScope.launch {
                    initChatState()
                }
            joinAll(nicknameJob, homeStateJob)
            reduce {
                state.copy(isLoading = false)
            }
        }

    private suspend fun initHomeState() =
        subIntent {
            collectDataState(
                flow = getHome(),
                onSuccess = {
                    reduce {
                        state.copy(
                            keyCount = it.keyCount,
                            ticketCount = it.ticketCount,
                            ticketLimit = it.ticketLimit,
                            newNotificationArrived = it.hasNewNotification,
                            newTimeCapsuleArrived = it.hasNewTimeCapsule,
                            newReportArrived = it.hasNewReport,
                            newReportId = it.newReportId,
                        )
                    }
                },
                onError = { throwable, code, data ->
                    reduce {
                        state.copy(isLoading = false)
                    }
                    Logger.e("HomeViewModel: init home state error: $throwable")
                    throw BaseException(
                        cause = throwable,
                        code = code,
                        message = throwable.message ?: "Home init error",
                    )
                },
            )
        }

    private suspend fun initNickname() =
        subIntent {
            collectDataState(
                flow = getUserNickname(),
                onSuccess = {
                    reduce {
                        state.copy(nickname = it)
                    }
                },
                onError = { throwable, code, data ->
                    reduce {
                        state.copy(isLoading = false)
                    }
                    Logger.e("HomeViewModel: init nickname error: $throwable")
                    throw BaseException(
                        cause = throwable,
                        code = code,
                        message = throwable.message ?: "Home nickname init error",
                    )
                },
            )
        }

    private suspend fun initChatState() = subIntent {
        getChatRoomSession().handle(
            onSuccess = { session ->
                reduce {
                    state.copy(
                        roomId = session.roomId,
                        isChatTempSaved = session.isTempSave
                    )
                }
            },
            onError = { throwable, code, data ->
                reduce {
                    state.copy(isLoading = false)
                }
                Logger.e("HomeViewModel: init chat state error: $throwable")
                throw BaseException(
                    cause = throwable,
                    code = code,
                    message = throwable.message ?: "Home init error",
                )
            }
        )
    }

    private fun handleEnterChat() =
        baseIntent {
            if (state.roomId == null) {
                throw IllegalStateException("chatRoomId is null")
            }

            if (state.isChatTempSaved) {
                postSideEffect(HomeSideEffect.EnterChatRoom(ChatEntry.Resume))
            } else if (state.ticketCount <= 0) {
                postSideEffect(HomeSideEffect.TicketNotEnough)
                return@baseIntent
            } else {
                postSideEffect(HomeSideEffect.EnterChatRoom(ChatEntry.New))
            }
        }

    private fun handleDeletePendingChat() =
        baseIntent {
            if (state.roomId == null) {
                throw IllegalStateException("chatRoomId is null")
            }

            reduce {
                state.copy(isLoading = true)
            }
            deleteChatRoom(state.roomId!!).handle(
                onSuccess = {
                    Logger.d("HomeViewModel: exitChatRoom success")
                    // refresh chat state
                    initChatState()
                },
                onError = { throwable, code, data ->
                    Logger.e("HomeViewModel: exitChatRoom failed $throwable")
                    throw BaseException(
                        cause = throwable,
                        code = code,
                        message = throwable.message ?: "Home exit chat error",
                    )
                },
            )
            reduce {
                state.copy(isLoading = false)
            }
        }
}
