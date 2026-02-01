package com.emotionstorage.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.R as authR
import com.emotionstorage.ui.R
import com.emotionstorage.auth.presentation.LoginAction
import com.emotionstorage.auth.presentation.LoginSideEffect
import com.emotionstorage.auth.presentation.LoginState
import com.emotionstorage.auth.presentation.LoginViewModel
import com.emotionstorage.auth.ui.component.SocialLoginButton
import com.emotionstorage.auth.ui.modal.InquireLoginErrorModal
import com.emotionstorage.auth.ui.modal.RetryHandleLoginModal
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.component.modal.TempErrorModal
import com.emotionstorage.ui.component.toast.AppSnackbarHost
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.buildHighlightAnnotatedString

private sealed class ModalState {
    object None : ModalState()

    data class RetryHandleLogin(
        val accessToken: String,
    ) : ModalState()

    data class InquireLoginError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
    ) : ModalState()

    object TempError : ModalState()
}

@Composable
fun LoginScreen(
    getGoogleIdToken: suspend () -> String,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
    navToOnBoarding: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
    val state = viewModel.container.stateFlow.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var modalState by remember {
        mutableStateOf<ModalState>(ModalState.None)
    }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is LoginSideEffect.LoginSuccess -> {
                    navToHome()
                }

                is LoginSideEffect.NeedSignUp -> {
                    navToOnBoarding(effect.provider, effect.idToken)
                }

                is LoginSideEffect.SocialTokenIssueError -> {
                    snackbarHostState.showSnackbar("소셜 로그인 토큰 발급 실패")
                }

                is LoginSideEffect.InvalidSocialTokenError -> {
                    snackbarHostState.showSnackbar("소셜 로그인 토큰 인증 실패")
                }

                is LoginSideEffect.RetryHandleLogin -> {
                    modalState =
                        ModalState.RetryHandleLogin(
                            effect.accessToken,
                        )
                }

                is LoginSideEffect.InquireLoginError -> {
                    modalState =
                        ModalState.InquireLoginError(
                            effect.errorCode,
                            effect.throwable,
                        )
                }

                is BaseSideEffect.NetworkError -> {
                    snackbarHostState.showSnackbar("연결이 잠시 불안정해요. \uD83D\uDE22\n인터넷 연결을 확인 후 다시 시도해주세요.")
                }

                is BaseSideEffect.TemporalError -> {
                    modalState = ModalState.TempError
                }
            }
        }
    }

    StatelessLoginScreen(
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        state = state.value,
        onAction = viewModel::onAction,
        getGoogleIdToken = getGoogleIdToken,
    )

    when (modalState) {
        is ModalState.None -> {}

        is ModalState.RetryHandleLogin -> {
            val retryState = modalState as ModalState.RetryHandleLogin
            RetryHandleLoginModal(
                onDismissRequest = {
                    modalState = ModalState.None
                },
                onConfirm = {
                    viewModel.onAction(
                        LoginAction.RetryLogin(
                            retryState.accessToken,
                        ),
                    )
                },
            )
        }

        is ModalState.InquireLoginError -> {
            val inquireModalState = modalState as ModalState.InquireLoginError
            InquireLoginErrorModal(
                inquireModalState.errorCode,
                inquireModalState.throwable,
                onDismissRequest = {
                    modalState = ModalState.None
                },
            )
        }

        is ModalState.TempError -> {
            TempErrorModal {
                modalState = ModalState.None
            }
        }
    }
}

@Composable
private fun StatelessLoginScreen(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
    getGoogleIdToken: suspend () -> String = { "" },
) {
    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize(),
        snackbarHost = {
            AppSnackbarHost(
                hostState = snackbarHostState,
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize()
                    .padding(padding),
        ) {
            if (state.isLoading) {
                LoadingOverlay()
            }

            // bg image
            Image(
                modifier =
                    Modifier
                        .zIndex(-20f)
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                painter = painterResource(id = authR.drawable.graphic_login_bg),
                contentDescription = null,
            )

            // mooi image
            Image(
                modifier =
                    Modifier
                        .zIndex(-10f)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .offset(y = (-148).dp),
                painter = painterResource(id = authR.drawable.graphic_login_mooi),
                contentDescription = null,
            )
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = 109.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.height(24.dp),
                    style = MooiTheme.typography.brandFont2,
                    color = Color.White,
                    text = stringResource(id = authR.string.login_title),
                )
                Image(
                    modifier = Modifier.size(209.dp, 105.dp),
                    painter =
                        painterResource(
                            id = R.drawable.graphic_logo,
                        ),
                    contentDescription = "Mooi logo",
                )
            }

            Column(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            MooiTheme.colorScheme.backgroundTinted,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        ).padding(top = 26.dp, bottom = 36.dp)
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    style = MooiTheme.typography.body5,
                    color = Color.White,
                    text =
                        buildHighlightAnnotatedString(
                            stringResource(authR.string.login_description),
                            listOf("당신의 이야기"),
                            SpanStyle(color = MooiTheme.colorScheme.primaryBlue500),
                        ),
                )
                Spacer(modifier = Modifier.height(26.dp))
                SocialLoginButton(
                    provider = AuthProvider.KAKAO,
                    onClick = {
                        onAction(LoginAction.KakaoLogin)
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
                SocialLoginButton(
                    provider = AuthProvider.GOOGLE,
                    onClick = {
                        onAction(LoginAction.GoogleLogin(getGoogleIdToken))
                    },
                )
            }
        }
    }
}

@PreviewScreenRatios
@Composable
private fun LoginScreenPreview() {
    MooiTheme {
        StatelessLoginScreen()
    }
}
