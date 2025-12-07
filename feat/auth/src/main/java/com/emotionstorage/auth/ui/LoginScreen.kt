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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
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
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.buildHighlightAnnotatedString

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
    navToOnBoarding: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
    val state = viewModel.container.stateFlow.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is LoginSideEffect.LoginSuccess -> {
                    navToHome()
                }

                is LoginSideEffect.NeedSignUp -> {
                    navToOnBoarding(effect.provider, effect.idToken)
                }

                is LoginSideEffect.LoginFailedWithException -> {
                    // todo: add error modal
                }
            }
        }
    }

    StatelessLoginScreen(
        modifier = modifier,
        state = state.value,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun StatelessLoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState = LoginState(),
    onAction: (LoginAction) -> Unit = {},
) {
    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize(),
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
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
                            MooiTheme.colorScheme.blueGrayBackground,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                        )
                        .padding(top = 26.dp, bottom = 36.dp)
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
                            SpanStyle(color = MooiTheme.colorScheme.primary),
                        ),
                )
                Spacer(modifier = Modifier.height(26.dp))
                SocialLoginButton(
                    provider = AuthProvider.KAKAO,
                    onClick = {
                        onAction(LoginAction.Login(AuthProvider.KAKAO))
                    },
                )
                Spacer(modifier = Modifier.height(12.dp))
                SocialLoginButton(
                    provider = AuthProvider.GOOGLE,
                    onClick = {
                        onAction(LoginAction.Login(AuthProvider.GOOGLE))
                    },
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun LoginScreenPreview() {
    MooiTheme {
        StatelessLoginScreen()
    }
}
