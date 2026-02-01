package com.emotionstorage.my.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.my.presentation.InputNicknameEvent
import com.emotionstorage.my.presentation.NicknameChangeViewModel
import com.emotionstorage.my.presentation.NicknameChangeViewModel.State.InputState
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.HideKeyboard
import com.emotionstorage.ui.component.text.TextInput
import com.emotionstorage.ui.component.text.TextInputState
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.component.modal.LoginSessionExpiredModal
import com.emotionstorage.ui.component.modal.TempErrorModal
import com.emotionstorage.ui.theme.MooiTheme

private enum class ModalState {
    None,
    TempError,
    LoginSessionExpired,
}

@Composable
fun NicknameChangeScreen(
    navToBack: () -> Unit,
    navToLogin: () -> Unit,
    viewModel: NicknameChangeViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsState()
    val (modalState, setModalState) = remember { mutableStateOf(ModalState.None) }

    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect {
            when (it) {
                is BaseSideEffect.TemporalError -> {
                    setModalState(ModalState.TempError)
                }

                is BaseSideEffect.SessionExpired -> {
                    setModalState(ModalState.LoginSessionExpired)
                }
            }
        }
    }

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

    when (modalState) {
        ModalState.None -> {
            // no modal
        }

        ModalState.TempError -> {
            TempErrorModal(
                onDismissRequest = { setModalState(ModalState.None) },
            )
        }

        ModalState.LoginSessionExpired -> {
            LoginSessionExpiredModal(
                onDismissRequest = { setModalState(ModalState.None) },
                navToLogin = navToLogin,
            )
        }
    }
}

@Composable
private fun StatelessNicknameChangeScreen(
    state: NicknameChangeViewModel.State,
    event: InputNicknameEvent,
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    Scaffold(
        topBar = {
            TopAppBar(
                showBackButton = true,
                showBackground = false,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        HideKeyboard(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
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

                val animatedPadding by animateDpAsState(
                    if (imeVisible) {
                        24.dp
                    } else {
                        39.dp
                    },
                    label = "padding",
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessVeryLow,
                        ),
                )

                CtaButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .navigationBarsPadding()
                            .imePadding()
                            .padding(
                                bottom = animatedPadding,
                            ),
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
            withStyle(SpanStyle(color = MooiTheme.colorScheme.primaryBlue500)) {
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

@PreviewScreenRatios
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
        helperMessage = "2~8자리의 한글 또는 영문을 사용해주세요",
    )

private val PreviewInvalid =
    NicknameChangeViewModel.State(
        nickname = "??",
        inputState = InputState.INVALID,
        helperMessage = "사용할 수 없는 이름입니다",
    )

private val PreviewValid =
    NicknameChangeViewModel.State(
        nickname = "모이",
        inputState = InputState.VALID,
        helperMessage = "사용 가능한 이름입니다.",
    )
