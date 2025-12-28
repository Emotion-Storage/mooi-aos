package com.emotionstorage.my.presentation

import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.useCase.auth.DeleteAccountUseCase
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
    ) : WithdrawNoticeEffect()
}

@HiltViewModel
class WithdrawNoticeViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
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
            postSideEffect(
                WithdrawNoticeEffect.WithdrawError(
                    ErrorCode.UNKNOWN,
                    Throwable(
                        "deleteAccountUseCase failed",
                    ),
                ),
            )
            try {
//                if (deleteAccountUseCase()) {
//                    postSideEffect(WithdrawNoticeEffect.WithDrawSuccess)
//                } else {
//                    postSideEffect(
//                        WithdrawNoticeEffect.WithdrawError(
//                            ErrorCode.UNKNOWN, Throwable(
//                                "deleteAccountUseCase failed"
//                            )
//                        )
//                    )
//                }
            } catch (t: Throwable) {
                Logger.e("deleteAccountUseCase error $t")
                postSideEffect(WithdrawNoticeEffect.WithdrawError(ErrorCode.UNKNOWN, t))
            }
        }
}
