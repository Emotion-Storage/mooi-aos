package com.emotionstorage.my.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.useCase.auth.DeleteAccountUseCase
import com.emotionstorage.domain.useCase.user.GetUserEmailAndNicknameUseCase
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class WithdrawNoticeState(
    val isLoading: Boolean = false,
)

sealed class WithdrawNoticeAction {
    object WithDraw : WithdrawNoticeAction()
}

sealed class WithdrawNoticeEffect : BaseSideEffect {
    object WithDrawSuccess : WithdrawNoticeEffect()

    data class WithdrawError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
        val userEmail: String? = null,
        val userNickname: String? = null,
    ) : WithdrawNoticeEffect()
}

@HiltViewModel
class WithdrawNoticeViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val getUserEmailAndNickname: GetUserEmailAndNicknameUseCase,
) : BaseViewModel<WithdrawNoticeState>(
        WithdrawNoticeState(),
    ) {
    fun onAction(action: WithdrawNoticeAction) {
        when (action) {
            is WithdrawNoticeAction.WithDraw -> {
                handleWithDraw()
            }
        }
    }

    private fun handleWithDraw() =
        intent {
            reduce {
                state.copy(isLoading = true)
            }
            try {
                if (deleteAccountUseCase()) {
                    postSideEffect(WithdrawNoticeEffect.WithDrawSuccess)
                    reduce {
                        state.copy(isLoading = false)
                    }
                } else {
                    throw Throwable("deleteAccountUseCase failed")
                }
            } catch (t: Throwable) {
                Logger.e("deleteAccountUseCase error $t")

                // get user email & nickname if possible
                var email: String? = null
                var nickname: String? = null
                runCatching {
                    getUserEmailAndNickname()?.let {
                        email = it.first
                        nickname = it.second
                    }
                }

                reduce {
                    state.copy(isLoading = false)
                }
                postSideEffect(
                    WithdrawNoticeEffect.WithdrawError(
                        ErrorCode.UNKNOWN,
                        t,
                        email,
                        nickname,
                    ),
                )
            }
        }
}
