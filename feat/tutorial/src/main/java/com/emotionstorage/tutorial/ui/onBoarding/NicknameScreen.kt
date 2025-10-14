package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.onBoarding.InputNicknameEvent
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel.State.InputState
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.HideKeyboard
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.component.TextInputState
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding step 1
 * - input user nickname
 */
@Composable
fun NicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: NicknameViewModel = hiltViewModel(),
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToGenderBirth: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    StatelessNicknameScreen(
        modifier = modifier,
        state = viewModel.state.collectAsState().value,
        event = viewModel.event,
        onNicknameInputComplete = onNicknameInputComplete,
        navToGenderBirth = navToGenderBirth,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessNicknameScreen(
    modifier: Modifier = Modifier,
    state: NicknameViewModel.State,
    event: InputNicknameEvent,
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToGenderBirth: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val (isExitModelOpen, setIsExitModelOpen) = remember { mutableStateOf(false) }
    OnBoardingExitModel(
        isModelOpen = isExitModelOpen,
        onDismissRequest = { setIsExitModelOpen(false) },
        onExit = navToBack,
    )

    val focusManager = LocalFocusManager.current

    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize(),
        topBar = {
            val onBackClick = { setIsExitModelOpen(true) }
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = onBackClick,
                handleBackPress = true,
                onHandleBackPress = onBackClick,
            )
        },
    ) { padding ->
        HideKeyboard {
            Column(
                modifier =
                    Modifier
                        .background(MooiTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(WindowInsets.navigationBars)
            ) {
                OnBoardingTitle(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    currentStep = 0,
                    title = stringResource(R.string.on_boarding_nickname_title),
                    titleHighlights =
                        stringResource(R.string.on_boarding_nickname_title_highlights).split(
                            ',',
                        ),
                )

                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 30.dp, horizontal = 16.dp),
                ) {
                    TextInput(
                        label = "이름",
                        value = state.nickname,
                        onValueChange = event::onNicknameChange,
                        showCharCount = true,
                        maxCharCount = 8,
                        state =
                            when (state.nicknameInputState) {
                                InputState.EMPTY -> TextInputState.Empty(infoMessage = state.nicknameHelperMessage)
                                InputState.INVALID -> TextInputState.Error(errorMessage = state.nicknameHelperMessage)
                                InputState.VALID -> TextInputState.Success(successMessage = state.nicknameHelperMessage)
                            },
                    )
                }

                val animatedPadding by animateDpAsState(
                    if (imeVisible) {
                        24.dp
                    } else {
                        39.dp
                    },
                    label = "padding"
                )
                CtaButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .imePadding()
                            .padding(horizontal = 15.dp)
                            .padding(
                                bottom = animatedPadding,
                            ),
                    labelString = "다음으로",
                    enabled = state.nicknameInputState == InputState.VALID,
                    onClick = {
                        focusManager.clearFocus()
                        onNicknameInputComplete(state.nickname)
                        navToGenderBirth()
                    },
                    isDefaultWidth = false
                )
            }
        }
    }
}

@Composable
private fun OnBoardingExitModel(
    isModelOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModelOpen) {
        Modal(
            topDescription = "지금 돌아가면 회원가입을\n다시 시작해야 해요.",
            title = "그래도 로그인 화면으로\n돌아갈까요?",
            confirmLabel = "회원가입을 계속 할게요.",
            dismissLabel = "로그인 화면으로 나갈래요.",
            onDismissRequest = onDismissRequest,
            onDismiss = onExit,
        )
    }
}

@PreviewScreenSizes
@Composable
private fun NicknameScreenPreview() {
    MooiTheme {
        StatelessNicknameScreen(
            state = NicknameViewModel.State(),
            event =
                object : InputNicknameEvent {
                    override fun onNicknameChange(nickname: String) {}
                },
        )
    }
}
