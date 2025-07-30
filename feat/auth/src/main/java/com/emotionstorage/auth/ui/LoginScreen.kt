package com.emotionstorage.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.presentation.LoginViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.auth.R
import com.emotionstorage.auth.ui.component.SocialLoginButton
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.theme.pretendard

@Composable
fun LoginScreen(
    navToHome: () -> Unit = {},
    navToOnBoarding: () -> Unit = {},
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    // 로그인 화면 와이어프레임 - 이미지 및 문구 변경 필요
    Scaffold(
        modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
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

            // todo: replace with app icon
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.Black)
                    .width(148.dp)
                    .height(148.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SocialLoginButton(
                    provider = AuthProvider.KAKAO,
                    onClick = {
                        loginViewModel.event.onLoginButtonClick(
                            AuthProvider.KAKAO,
                            navToHome,
                            navToOnBoarding
                        )
                    })
                SocialLoginButton(
                    provider = AuthProvider.GOOGLE,
                    onClick = {
                        loginViewModel.event.onLoginButtonClick(
                            AuthProvider.GOOGLE,
                            navToHome,
                            navToOnBoarding
                        )
                    })
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MooiTheme {
        LoginScreen()
    }
}