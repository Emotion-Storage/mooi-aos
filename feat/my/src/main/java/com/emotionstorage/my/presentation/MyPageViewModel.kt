package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.auth.DeleteAccountUseCase
import com.emotionstorage.domain.useCase.auth.LogoutUseCase
import com.emotionstorage.domain.useCase.myPage.GetMyPageOverviewUseCase
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

    object AccountInfo : MyPageAction()

    object TermsAndPrivacy : MyPageAction()

    object CopyEmail : MyPageAction()

    object WithDrawConfirm : MyPageAction()
}

sealed class MyPageSideEffect {
    object LogoutSuccess : MyPageSideEffect()

    object NavigateToNicknameChange : MyPageSideEffect()

    object EmailCopied : MyPageSideEffect()

    object NavigateToAccountInfo : MyPageSideEffect()

    object NavigateToKeyDescription : MyPageSideEffect()

    object NavigateToTermsAndPrivacy : MyPageSideEffect()

    object NavigateToWithDrawNotice : MyPageSideEffect()

    object WithDrawSuccess : MyPageSideEffect()

    object NavigateToSplash : MyPageSideEffect()

    data class ShowToast(
        val message: String,
    ) : MyPageSideEffect()
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val myPageOverviewUseCase: GetMyPageOverviewUseCase,
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

            is MyPageAction.AccountInfo -> {
                handleAccountInfo()
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
            myPageOverviewUseCase().collect { result ->
                when (result) {
                    is DataState.Success -> {
                        reduce {
                            state.copy(
                                nickname = result.data.nickname,
                                signupDday = result.data.days,
                                keyCount = result.data.keys,
                            )
                        }
                    }

                    is DataState.Error -> {
                        postSideEffect(MyPageSideEffect.ShowToast(result.throwable.message ?: "마이페이지 불러오기 실패"))
                    }

                    is DataState.Loading -> {
                        // do nothing
                    }
                }
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
            postSideEffect(MyPageSideEffect.NavigateToKeyDescription)
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
            try {
                deleteAccountUseCase()
                postSideEffect(MyPageSideEffect.WithDrawSuccess)
                postSideEffect(MyPageSideEffect.NavigateToSplash)
            } catch (t: Throwable) {
                postSideEffect(MyPageSideEffect.ShowToast(t.message ?: "회원탈퇴 실패"))
            }
        }

    private fun handleAccountInfo() =
        intent {
            postSideEffect(MyPageSideEffect.NavigateToAccountInfo)
        }
}
