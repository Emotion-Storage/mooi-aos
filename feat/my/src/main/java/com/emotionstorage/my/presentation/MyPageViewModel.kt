package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.useCase.auth.LogoutUseCase
import com.emotionstorage.my.BuildConfig
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
)

sealed class MyPageAction {
    object Initiate : MyPageAction()

    object Logout : MyPageAction()

    object NicknameChange : MyPageAction()

    object KeyDescription : MyPageAction()

    object TermsAndPrivacy : MyPageAction()

    object CopyEmail : MyPageAction()

    object WithDrawConfirm : MyPageAction()
}

sealed class MyPageSideEffect {
    object LogoutSuccess : MyPageSideEffect()

    object NavigateToNicknameChange : MyPageSideEffect()

    object EmailCopied : MyPageSideEffect()

    object NavigateToTermsAndPrivacy : MyPageSideEffect()

    object NavigateToWithDrawNotice : MyPageSideEffect()

    object WithDrawSuccess : MyPageSideEffect()

    data class NavigateToKeyDescription(
        val keyCount: Int,
    ) : MyPageSideEffect()

    data class ShowToast(
        val message: String,
    ) : MyPageSideEffect()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel(),
    ContainerHost<MyPageState, MyPageSideEffect> {
    override val container = container<MyPageState, MyPageSideEffect>(MyPageState())

    fun onAction(action: MyPageAction) {
        when (action) {
            is MyPageAction.Initiate -> {
                handleInitiate()
            }

            is MyPageAction.NicknameChange -> {
                handleNicknameChange()
            }

            is MyPageAction.Logout -> {
                handleLogout()
            }

            is MyPageAction.KeyDescription -> {
                handleKeyDescription()
            }

            is MyPageAction.CopyEmail -> {
                handleCopyEmail()
            }

            is MyPageAction.TermsAndPrivacy -> {
                handleTermsAndPrivacy()
            }

            is MyPageAction.WithDrawConfirm -> {
                handleWithDraw()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            // todo: call use case
            reduce {
                state.copy(
                    nickname = "찡찡이",
                    signupDday = 280,
                    keyCount = 5,
                    versionName = "1.0.0",
                )
            }
        }

    private fun handleLogout() =
        intent {
            try {
                logoutUseCase()
                postSideEffect(MyPageSideEffect.LogoutSuccess)
            } catch (t: Throwable) {
                postSideEffect(MyPageSideEffect.ShowToast(t.message ?: "로그아웃 실패"))
            }
        }

    private fun handleNicknameChange() =
        intent {
            postSideEffect(MyPageSideEffect.NavigateToNicknameChange)
        }

    private fun handleKeyDescription() =
        intent {
            postSideEffect(MyPageSideEffect.NavigateToKeyDescription(state.keyCount))
        }

    private fun handleCopyEmail() =
        intent {
            postSideEffect(MyPageSideEffect.EmailCopied)
        }

    private fun handleTermsAndPrivacy() =
        intent {
            postSideEffect(MyPageSideEffect.NavigateToTermsAndPrivacy)
        }

    private fun handleWithDraw() =
        intent {
            postSideEffect(MyPageSideEffect.WithDrawSuccess)
        }
}
