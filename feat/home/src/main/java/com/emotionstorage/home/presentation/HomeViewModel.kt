package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.useCase.chat.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.user.GetUserNicknameUseCase
import com.emotionstorage.domain.useCase.home.GetHomeUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(
    val nickname: String = "",
    val keyCount: Int? = null,
    val ticketCount: Int = 3,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
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
                getHome().collect {
                    when (it) {
                        is DataState.Success -> {
                            reduce {
                                state.copy(
                                    keyCount = it.data.keyCount,
                                    ticketCount = it.data.ticketCount,
                                    newNotificationArrived = it.data.hasNewNotification,
                                    newTimeCapsuleArrived = it.data.hasNewTimeCapsule,
                                    newReportArrived = it.data.hasNewReport,
                                )
                            }
                        }

                        is DataState.Error -> {
                            Logger.e("HomeViewModel: handleUpdateState error: $it")
                        }

                        is DataState.Loading -> {
                            // do nothing
                        }
                    }
                }
            }

        private suspend fun initNickname() =
            subIntent {
                getUserNickname().collect {
                    when (it) {
                        is DataState.Success -> {
                            reduce {
                                state.copy(nickname = it.data)
                            }
                        }

                        is DataState.Error -> {
                            Logger.e("HomeViewModel: handleInitNickname error: $it")
                        }

                        is DataState.Loading -> {
                            // do nothing
                        }
                    }
                }
            }

        private fun handleEnterChat() =
            intent {
                getChatRoomId().collect {
                    if (it is DataState.Success) {
                        postSideEffect(HomeSideEffect.EnterCharRoomSuccess(it.data))
                    }
                    if (it is DataState.Error) {
                        Logger.e("HomeViewModel: handleEnterChat error: $it")
                    }
                }
            }
    }
