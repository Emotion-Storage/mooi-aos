package com.emotionstorage.home.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.ai_chat.domain.usecase.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.home.domain.usecase.GetHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeState(
    val keyCount: Int = 0,
    val newNotificationArrived: Boolean = false,
    val newTimeCapsuleArrived: Boolean = false,
    val newReportArrived: Boolean = false,
)

sealed class HomeAction {
    object Init : HomeAction()
    object EnterChat : HomeAction()
}

sealed class HomeSideEffect {
    data class EnterCharRoomSuccess(val roomId: String) : HomeSideEffect()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHome: GetHomeUseCase,
    private val getChatRoomId: GetChatRoomIdUseCase
) : ViewModel(), ContainerHost<HomeState, HomeSideEffect> {
    override val container = container<HomeState, HomeSideEffect>(HomeState())

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Init -> {
                handleInit()
            }

            is HomeAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInit() = intent {
        getHome().collectLatest {
            if (it is DataState.Success) {
                reduce {
                    state.copy(
                        keyCount = it.data.keyCount,
                        newNotificationArrived = it.data.hasNewNotification,
                        newTimeCapsuleArrived = it.data.hasNewTimeCapsule,
                        newReportArrived = it.data.hasNewReport
                    )
                }
            }
        }
    }

    private fun handleEnterChat() = intent {
        getChatRoomId().collectLatest {
            if (it is DataState.Success) {
                postSideEffect(HomeSideEffect.EnterCharRoomSuccess(it.data))
            }
        }
    }
}