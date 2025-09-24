package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.GetUserNicknameUseCase
import com.emotionstorage.home.domain.usecase.GetHomeUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(
    val nickname: String = "",
    val keyCount: Int = 0,
    val ticketCount: Int = 3,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
)

sealed class HomeAction {
    object InitNickname : HomeAction()
    object Initiate : HomeAction()
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
            is HomeAction.InitNickname -> {
                handleInitNickname()
            }

            is HomeAction.Initiate -> {
                handleInitiate()
            }

            is HomeAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInitNickname() = intent {
        getUserNickname().collect {
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

    private fun handleInitiate() = intent {
        getHome().collect {
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
        getChatRoomId().collect {
            Logger.d("HomeViewModel: handleEnterChat: ${it}")
            if (it is DataState.Success) {
                postSideEffect(HomeSideEffect.EnterCharRoomSuccess(it.data))
            }
            // 에러 발생 하는 경우 화면 넘기는 용 (추후 삭제)
            if (it is DataState.Error) {
                Logger.e("HomeViewModel: handleEnterChat error: ${it}")
                postSideEffect(HomeSideEffect.EnterCharRoomSuccess("123"))
            }
        }
    }
}