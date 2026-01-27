package com.emotionstorage.my.presentation

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.auth.LogoutUseCase
import com.emotionstorage.domain.useCase.myPage.GetMyPageOverviewUseCase
import com.emotionstorage.my.BuildConfig
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MyPageState(
    val nickname: String = "",
    val signupDday: Int = 0,
    val keyCount: Int = 0,
    val replyEmail: String = BuildConfig.MOOI_REPLY_EMAIL,
    // todo: 버전명 동적으로 표시되어야 함
    val versionName: String = "0.0.0",
    val isLoading: Boolean = false,
)

sealed class MyPageAction {
    object Initiate : MyPageAction()

    object Logout : MyPageAction()
}

sealed class MyPageSideEffect () : BaseSideEffect {
    object LogoutSuccess : MyPageSideEffect()

    object LogoutError : MyPageSideEffect()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logout: LogoutUseCase,
    private val getMyPageOverview: GetMyPageOverviewUseCase,
) : BaseViewModel<MyPageState>(MyPageState()) {

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
        baseIntent {
            getMyPageOverview().collect { result ->
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
                        throw BaseException(
                            cause = result.throwable,
                            code = result.code,
                            message = result.throwable.message ?: "MyPageViewModel: Initiate error"
                        )
                    }

                    is DataState.Loading -> {
                        reduce { state.copy(isLoading = true) }
                    }
                }
            }
        }

    private fun handleLogout() =
        baseIntent {
            try {
                if (logout()) {
                    postSideEffect(MyPageSideEffect.LogoutSuccess)
                } else {
                    postSideEffect(MyPageSideEffect.LogoutError)
                }
            } catch (t: Throwable) {
                Logger.e("LogoutUseCase error: $t")
                postSideEffect(MyPageSideEffect.LogoutError)
            }
        }
}
