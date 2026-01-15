package com.emotionstorage.auth.ui

import SpeechBubble
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.R
import com.emotionstorage.auth.presentation.SignupCompleteAction
import com.emotionstorage.auth.presentation.SignupCompleteSideEffect
import com.emotionstorage.auth.presentation.SignupCompleteViewModel
import com.emotionstorage.auth.ui.modal.RetryLoginModal
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.appBar.TopAppBar
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
    var showRetryLoginModal by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is SignupCompleteSideEffect.LoginSuccess -> {
                    navToHome()
                }

                is SignupCompleteSideEffect.LoginFailed -> {
                    showRetryLoginModal = true
                }
            }
        }
    }

    StatelessSignupCompleteScreen(
        modifier = modifier,
        onLogin = { viewModel.onAction(SignupCompleteAction.LoginWithIdToken(provider, idToken)) },
    )

    if (showRetryLoginModal) {
        RetryLoginModal(
            onDismissRequest = {
                showRetryLoginModal = false
            },
            onConfirm = {
                navToLogin()
            },
        )
    }
}

@Composable
private fun StatelessSignupCompleteScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.backgroundDefault)
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                showBackground = false,
            )
        },
    ) { padding ->
        BoxWithConstraints(
            Modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize()
                .padding(padding)
        ) {
            val safePadding = WindowInsets.safeDrawing.asPaddingValues()
            val topInset = safePadding.calculateTopPadding()
            val bottomInset = safePadding.calculateBottomPadding()
            val safeHeight = (maxHeight - topInset - bottomInset).coerceAtLeast(0.dp)

            Image(
                modifier = Modifier
                    .sizeIn(
                        maxWidth = Dp.Unspecified,
                        maxHeight = if (maxWidth >= 600.dp) safeHeight * 0.83f else safeHeight * 1.1f,
                    )
                    .fillMaxWidth()
                    .aspectRatio(360f / 752f),
                painter = painterResource(R.drawable.graphic_signup_complete),
                contentScale = ContentScale.Fit,
                contentDescription = null,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("가입을 환영해요.\n")
                            withStyle(SpanStyle(color = MooiTheme.colorScheme.primaryBlue500)) {
                                append("당신의 감정")
                            }
                            append("을,\n")
                            append("이곳에 천천히 담아보세요.")
                        },
                        style = MooiTheme.typography.head1.copy(fontWeight = FontWeight.SemiBold),
                        color = Color.White,
                    )
                    Text(
                        text = "여기부터 당신만의 기록이 시작돼요.",
                        style = MooiTheme.typography.body2,
                        color = MooiTheme.colorScheme.gray500,
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SpeechBubble(
                        modifier = Modifier.background(
                            MooiTheme.colorScheme.backgroundDefault.copy(alpha = 0.8f),
                            RoundedCornerShape(16.dp)
                        ),
                        contentText = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
                        textStyle = MooiTheme.typography.caption3.copy(lineHeight = 20.sp),
                        tail = BubbleTail.BottomCenter,
                        sizeParam = DpSize(265.dp, 84.dp),
                    )

                    CtaButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 39.dp),
                        labelString = "메인 화면으로 이동",
                        onClick = {
                            onLogin()
                        },
                        isDefaultWidth = false,
                    )
                }
            }
        }
    }
}

@PreviewScreenRatios
@Composable
private fun SignupCompleteScreenPreview() {
    MooiTheme {
        StatelessSignupCompleteScreen()
    }
}
