package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.auth.LogoutUseCase
import com.emotionstorage.domain.useCase.myPage.GetMyPageOverviewUseCase
import com.emotionstorage.my.BuildConfig
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class MyPageState(
    val nickname: String = "",
    val signupDday: Int = 0,
    val keyCount: Int = 0,
    val replyEmail: String = BuildConfig.MOOI_REPLY_EMAIL,
    val versionName: String = "0.0.0",
    val isLoading: Boolean = false,
)

sealed class MyPageAction {
    object Initiate : MyPageAction()

    object Logout : MyPageAction()
}

sealed class MyPageSideEffect {
    object LogoutSuccess : MyPageSideEffect()

    object LogoutError : MyPageSideEffect()

    data class ShowToast(
        val message: String,
    ) : MyPageSideEffect()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val myPageOverviewUseCase: GetMyPageOverviewUseCase,
) : ViewModel(),
    ContainerHost<MyPageState, MyPageSideEffect> {
    override val container = container<MyPageState, MyPageSideEffect>(MyPageState())

    fun onAction(action: MyPageAction) {
        when (action) {
            is MyPageAction.Initiate -> {
                handleInitiate()
            }

            is MyPageAction.Logout -> {
                handleLogout()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            myPageOverviewUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        reduce {
                            state.copy(
                                nickname = result.data.nickname,
                                signupDday = result.data.days,
                                keyCount = result.data.keys,
                                isLoading = false,
                            )
                        }
                    }

                    is DataState.Error -> {
                        reduce { state.copy(isLoading = false) }
                        postSideEffect(MyPageSideEffect.ShowToast(result.throwable.message ?: "마이페이지 불러오기 실패"))
                    }

                    is DataState.Loading -> {
                        reduce { state.copy(isLoading = true) }
                    }
                }
            }
        }

    private fun handleLogout() =
        intent {
            try {
                if (logoutUseCase()) {
                    postSideEffect(MyPageSideEffect.LogoutSuccess)
                } else
                    {
                        postSideEffect(MyPageSideEffect.LogoutError)
                    }
            } catch (t: Throwable) {
                Logger.e("LogoutUseCase error: $t")
                postSideEffect(MyPageSideEffect.LogoutError)
            }
        }
}
