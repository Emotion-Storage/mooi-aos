package com.emotionstorage.home.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.chat.ClearTempSavedRoomIdUseCase
import com.emotionstorage.domain.useCase.chat.GetChatRoomIdUseCase
import com.emotionstorage.domain.useCase.chat.ObserveTempSaveRoomIdUseCase
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
    val ticketCount: Int = 0,
    val ticketLimit: Int = 0,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
    val newReportId: Long? = null,
    val tempSavedRoomId: Long? = null,
    val showResumeChatModal: Boolean = false,
)

sealed class HomeAction {
    object Initiate : HomeAction()

    object EnterChat : HomeAction()

    object DismissResumeChatModal : HomeAction()

    object ResumeTempChat : HomeAction()

    object StartNewChatIgnoringTemp : HomeAction()
}

sealed class HomeSideEffect : BaseSideEffect {
    object TicketNotEnough : HomeSideEffect()

    data class EnterChatRoom(
        val roomId: Long,
    ) : HomeSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getUserNickname: GetUserNicknameUseCase,
        private val getHome: GetHomeUseCase,
        private val getChatRoomId: GetChatRoomIdUseCase,
        private val observeTempSavedRoomId: ObserveTempSaveRoomIdUseCase,
        private val clearTempSavedRoomId: ClearTempSavedRoomIdUseCase,
    ) : BaseViewModel<HomeState>(
            HomeState(),
        ) {
        private var observeRoomIdJobStarted = false

        fun onAction(action: HomeAction) {
            when (action) {
                is HomeAction.Initiate -> {
                    handleInitiate()
                }

                is HomeAction.EnterChat -> {
                    handleEnterChat()
                }

                is HomeAction.ResumeTempChat -> {
                    handleResumeTempChat()
                }

                is HomeAction.DismissResumeChatModal -> {
                    handleDismissResumeChatModal()
                }

                is HomeAction.StartNewChatIgnoringTemp -> {
                    handleStartNewChatIgnoringTemp()
                }
            }
        }

        private fun handleInitiate() =
            baseIntent {
                reduce {
                    state.copy(isLoading = true)
                }

                if (!observeRoomIdJobStarted) {
                    observeRoomIdJobStarted = true
                    baseViewModelScope.launch { observeTempSavedRoomIdState() }
                }

                val job1 =
                    baseViewModelScope.launch {
                        initNickname()
                    }
                val job2 =
                    baseViewModelScope.launch {
                        initHomeState()
                    }
                joinAll(job1, job2)
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

        private fun handleEnterChat() =
            baseIntent {
                if (state.ticketCount <= 0) {
                    postSideEffect(HomeSideEffect.TicketNotEnough)
                    return@baseIntent
                }

                if (state.tempSavedRoomId != null) {
                    reduce { state.copy(showResumeChatModal = true) }
                    return@baseIntent
                }

                getChatRoomId().handle(
                    onSuccess = {
                        postSideEffect(HomeSideEffect.EnterChatRoom(it))
                    },
                    onError = { throwable, code, data ->
                        Logger.e("HomeViewModel: handleEnterChat error: $throwable $code $data")
                        if (code == ErrorCode.TICKET_NOT_ENOUGH) {
                            postSideEffect(HomeSideEffect.TicketNotEnough)
                        } else {
                            throw BaseException(
                                cause = throwable,
                                code = code,
                                message = throwable.message ?: "Home enter chat error",
                            )
                        }
                    },
                )
            }

        private suspend fun observeTempSavedRoomIdState() =
            subIntent {
                observeTempSavedRoomId().collect { roomId ->
                    reduce { state.copy(tempSavedRoomId = roomId) }
                }
            }

        private fun handleDismissResumeChatModal() =
            baseIntent {
                reduce { state.copy(showResumeChatModal = false) }
            }

        private fun handleResumeTempChat() =
            baseIntent {
                val roomId =
                    state.tempSavedRoomId ?: run {
                        reduce { state.copy(showResumeChatModal = false) }
                        return@baseIntent
                    }

                clearTempSavedRoomId()
                reduce { state.copy(showResumeChatModal = false) }
                postSideEffect(HomeSideEffect.EnterChatRoom(roomId))
            }

        private fun handleStartNewChatIgnoringTemp() =
            baseIntent {
                clearTempSavedRoomId()
                reduce { state.copy(showResumeChatModal = false) }

                // 우선 boiler-plate code로 테스트
                getChatRoomId().handle(
                    onSuccess = {
                        postSideEffect(HomeSideEffect.EnterChatRoom(it))
                    },
                    onError = { throwable, code, data ->
                        Logger.e("HomeViewModel: handleEnterChat error: $throwable $code $data")
                        if (code == ErrorCode.TICKET_NOT_ENOUGH) {
                            postSideEffect(HomeSideEffect.TicketNotEnough)
                        } else {
                            throw BaseException(
                                cause = throwable,
                                code = code,
                                message = throwable.message ?: "Home enter chat error",
                            )
                        }
                    },
                )
            }
    }
