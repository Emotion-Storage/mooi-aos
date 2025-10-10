package com.emotionstorage.my.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.InputNicknameEvent
import com.emotionstorage.my.presentation.NicknameChangeViewModel
import com.emotionstorage.my.presentation.NicknameChangeViewModel.State.InputState
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.component.TextInputState
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun NicknameChangeScreen(
    viewModel: NicknameChangeViewModel = hiltViewModel(),
    navToBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()

    StatelessNicknameChangeScreen(
        state = state.value,
        event = viewModel as InputNicknameEvent,
        onNicknameInputComplete = {
            viewModel.submit {
                navToBack()
            }
        },
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessNicknameChangeScreen(
    state: NicknameChangeViewModel.State,
    event: InputNicknameEvent,
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                showBackButton = true,
                showBackground = false,
                handleBackPress = true,
                onBackClick = navToBack,
                onHandleBackPress = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .imePadding()
                        .consumeWindowInsets(WindowInsets.navigationBars),
            ) {
                NicknameChangeTitle()

                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 30.dp),
                ) {
                    TextInput(
                        label = "새 이름",
                        value = state.nickname,
                        onValueChange = event::onNicknameChange,
                        showCharCount = true,
                        placeHolder = "최소 2글자 이상의 이름을 적어주세요",
                        maxCharCount = 8,
                        state =
                            when (state.inputState) {
                                InputState.EMPTY -> {
                                    TextInputState.Empty(infoMessage = state.helperMessage)
                                }

                                InputState.INVALID -> {
                                    TextInputState.Error(errorMessage = state.helperMessage)
                                }

                                InputState.VALID -> {
                                    TextInputState.Success(successMessage = state.helperMessage)
                                }
                            },
                    )
                }

                CtaButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 40.dp),
                    labelString = "변경하기",
                    isDefaultWidth = false,
                    enabled = state.inputState == InputState.VALID && !state.submitting,
                    onClick = { onNicknameInputComplete(state.nickname) },
                )
            }
        }
    }
}

@Composable
fun NicknameChangeTitle() {
    val base =
        MooiTheme.typography.head1

    val title =
        buildAnnotatedString {
            withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
                append("어떤 이름")
            }
            append("으로\n")
            append("새롭게 불러드릴까요?")
        }

    Text(
        text = title,
        style = base,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, top = 22.dp, end = 16.dp),
    )
}

@Preview
@Composable
fun NicknameChangeScreenPreview() {
    MooiTheme {
        StatelessNicknameChangeScreen(
            state = PreviewEmpty,
            event = NoopInputNicknameEvent,
        )

        StatelessNicknameChangeScreen(
            state = PreviewInvalid,
            event = NoopInputNicknameEvent,
        )

        StatelessNicknameChangeScreen(
            state = PreviewValid,
            event = NoopInputNicknameEvent,
        )
    }
}

// For Preview
private val NoopInputNicknameEvent =
    object : InputNicknameEvent {
        override fun onNicknameChange(input: String) = Unit
    }

// 대표 상태들
private val PreviewEmpty =
    NicknameChangeViewModel.State(
        nickname = "",
        inputState = InputState.EMPTY,
        helperMessage = "닉네임을 입력해 주세요",
    )

private val PreviewInvalid =
    NicknameChangeViewModel.State(
        nickname = "??",
        inputState = InputState.INVALID,
        helperMessage = "허용되지 않는 문자예요",
    )

private val PreviewValid =
    NicknameChangeViewModel.State(
        nickname = "모이",
        inputState = InputState.VALID,
        helperMessage = "멋진 닉네임이네요!",
    )
