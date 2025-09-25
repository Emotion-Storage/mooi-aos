package com.emotionstorage.auth.ui

import SpeechBubble
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.presentation.SignupCompleteAction
import com.emotionstorage.auth.presentation.SignupCompleteSideEffect
import com.emotionstorage.auth.presentation.SignupCompleteViewModel
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SignupCompleteScreen(
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    viewModel: SignupCompleteViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
    navToLogin: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is SignupCompleteSideEffect.LoginSuccess -> {
                    navToHome()
                }
                is SignupCompleteSideEffect.LoginFailed -> {
                    navToLogin()
                }
            }
        }
    }

    StatelessSignupCompleteScreen(
        modifier = modifier,
        onLogin = { viewModel.onAction(SignupCompleteAction.LoginWithIdToken(provider, idToken)) },
    )
}

@Composable
private fun StatelessSignupCompleteScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
) {
    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize(),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .imePadding(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.padding(top = 62.dp),
                text =
                    buildAnnotatedString {
                        append("가입을 환영해요.\n이제 ")
                        withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                            append("당신의 이야기")
                        }
                        append("를\n")
                        withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                            append("우리만의 공간")
                        }
                        append("에 담아보세요.")
                    },
                style = MooiTheme.typography.head1.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White,
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SpeechBubble(
                    text = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
                )

                CtaButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 39.dp),
                    label = "메인화면으로 이동",
                    onClick = {
                        onLogin()
                    },
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SignupCompleteScreenPreview() {
    MooiTheme {
        StatelessSignupCompleteScreen()
    }
}
