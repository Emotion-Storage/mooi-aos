package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.useCase.chat.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.user.GetUserNicknameUseCase
import com.emotionstorage.domain.useCase.home.GetHomeUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(
    val isNicknameLoading: Boolean = false,
    val isHomeLoading: Boolean = false,
    val isEnterChatLoading: Boolean = false,
    val nickname: String = "",
    val keyCount: Int? = null,
    val ticketCount: Int = 0,
    val ticketLimit: Int = 0,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
    val newReportId: Long? = null,
)

sealed class HomeAction {
    object Initiate : HomeAction()

    object EnterChat : HomeAction()
}

sealed class HomeSideEffect {
    data class EnterCharRoomSuccess(
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
    ) : ViewModel(),
        ContainerHost<HomeState, HomeSideEffect> {
        override val container = container<HomeState, HomeSideEffect>(HomeState())

        fun onAction(action: HomeAction) {
            when (action) {
                is HomeAction.Initiate -> {
                    handleInitiate()
                }

                is HomeAction.EnterChat -> {
                    handleEnterChat()
                }
            }
        }

        private fun handleInitiate() =
            intent {
                initNickname()
                initHomeState()
            }

        private suspend fun initHomeState() =
            subIntent {
                collectDataState(
                    flow = getHome(),
                    onLoading = {
                        reduce {
                            state.copy(isHomeLoading = it)
                        }
                    },
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
                    onError = { throwable, _ ->
                        Logger.e("HomeViewModel: handleUpdateState error: $throwable")
                    },
                )
            }

        private suspend fun initNickname() =
            subIntent {
                collectDataState(
                    flow = getUserNickname(),
                    onLoading = {
                        reduce {
                            state.copy(isNicknameLoading = it)
                        }
                    },
                    onSuccess = {
                        reduce {
                            state.copy(nickname = it)
                        }
                    },
                    onError = { throwable, _ ->
                        Logger.e("HomeViewModel: handleInitNickname error: $throwable")
                    },
                )
            }

        private fun handleEnterChat() =
            intent {
                require(state.ticketCount > 0)
                collectDataState(
                    flow = getChatRoomId(),
                    onLoading = {
                        reduce{
                            state.copy(isEnterChatLoading = it)
                        }
                    },
                    onSuccess = {
                        postSideEffect(HomeSideEffect.EnterCharRoomSuccess(it))
                    },
                    onError = { throwable, _ ->
                        Logger.e("HomeViewModel: handleEnterChat error: $throwable")
                        // todo: add error popup
                    },
                )
            }
    }
