package com.emotionstorage.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.presentation.LoginViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.auth.R
import com.emotionstorage.auth.presentation.LoginAction
import com.emotionstorage.auth.presentation.LoginSideEffect
import com.emotionstorage.auth.ui.component.SocialLoginButton
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.theme.pretendard

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
    navToOnBoarding: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
) {
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is LoginSideEffect.LoginSuccess -> {
                    navToHome()
                }

                is LoginSideEffect.LoginFailed -> {
                    navToOnBoarding(effect.provider, effect.idToken)
                }

                is LoginSideEffect.LoginFailedWithException -> {
                    // do nothing
                }
            }
        }
    }

    StatelessLoginScreen(
        modifier = modifier,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun StatelessLoginScreen(
    modifier: Modifier = Modifier,
    onAction: (LoginAction) -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 67.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.height(37.dp),
                    style = MooiTheme.typography.body1,
                    color = MooiTheme.colorScheme.primary,
                    text = stringResource(id = R.string.login_title)
                )
                Text(
                    modifier = Modifier.height(56.dp),
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Medium,
                        fontSize = 40.sp,
                        letterSpacing = (-0.02).em,
                        lineHeight = (40 * 1.4).sp

                    ),
                    text = stringResource(id = R.string.login_app_name)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .background(Color.Black)
                    .width(148.dp)
                    .height(148.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialLoginButton(
                    provider = AuthProvider.KAKAO,
                    onClick = {
                        onAction(LoginAction.Login(AuthProvider.KAKAO))
                    })
                SocialLoginButton(
                    provider = AuthProvider.GOOGLE,
                    onClick = {
                        onAction(LoginAction.Login(AuthProvider.GOOGLE))
                    })
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