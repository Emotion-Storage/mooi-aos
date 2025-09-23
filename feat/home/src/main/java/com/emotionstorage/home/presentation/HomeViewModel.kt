package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.GetUserNicknameUseCase
import com.emotionstorage.home.domain.usecase.GetHomeUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(
    val nickname: String = "",
    val keyCount: Int = 0,
    val ticketCount: Int = 0,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
)

sealed class HomeAction {
    object InitNickName : HomeAction()
    object UpdateState : HomeAction()
    object EnterChat : HomeAction()
}

sealed class HomeSideEffect {
    data class EnterCharRoomSuccess(val roomId: String) : HomeSideEffect()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserNickname: GetUserNicknameUseCase,
    private val getHome: GetHomeUseCase,
    private val getChatRoomId: GetChatRoomIdUseCase
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
    override val container = container<HomeState, HomeSideEffect>(HomeState())

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.InitNickName -> {
                handleInitNickname()
            }

            is HomeAction.UpdateState -> {
                handleUpdateState()
            }

            is HomeAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInitNickname() = intent {
        getUserNickname().collectLatest {
            when(it){
                is DataState.Success -> {
                    Logger.d("HomeViewModel: handleInitNickname: ${it}")
                    reduce {
                        state.copy(nickname = it.data)
                    }
                }
                is DataState.Error -> {
                    Logger.e("HomeViewModel: handleInitNickname error: ${it}")
                }
                is DataState.Loading -> {
                    // do nothing
                }
            }
        }
    }

    private fun handleUpdateState() = intent {
        getHome().collectLatest {
            when (it) {
                is DataState.Success -> {
                    Logger.d("HomeViewModel: handleUpdateState: ${it}")
                    reduce {
                        state.copy(
                            keyCount = it.data.keyCount,
                            ticketCount = it.data.ticketCount,
                            newNotificationArrived = it.data.hasNewNotification,
                            newTimeCapsuleArrived = it.data.hasNewTimeCapsule,
                            newReportArrived = it.data.hasNewReport
                        )
                    }
                }

                is DataState.Error -> {
                    Logger.e("HomeViewModel: handleUpdateState error: ${it}")
                }

                is DataState.Loading -> {
                    // do nothing
                }
            }
        }
    }

    private fun handleEnterChat() = intent {
        getChatRoomId().collectLatest {
            Logger.d("HomeViewModel: handleEnterChat: ${it}")
            if (it is DataState.Success) {
                postSideEffect(HomeSideEffect.EnterCharRoomSuccess(it.data))
            }
        }
    }
}