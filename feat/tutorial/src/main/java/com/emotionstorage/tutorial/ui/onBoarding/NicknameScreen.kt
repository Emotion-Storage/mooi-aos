package com.emotionstorage.tutorial.ui.onBoarding

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.emotionstorage.tutorial.R as tutorialR
import com.emotionstorage.tutorial.presentation.onBoarding.InputNicknameEvent
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel.State.InputState
import com.emotionstorage.tutorial.ui.component.OnBoardingTitle
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.HideKeyboard
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.text.TextInput
import com.emotionstorage.ui.component.text.TextInputState
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding step 1
 * - input user nickname
 */
@Composable
fun NicknameScreen(
    modifier: Modifier = Modifier,
    nickname: String? = null,
    viewModel: NicknameViewModel = hiltViewModel(),
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToGenderBirth: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    LaunchedEffect("init") {
        // init nickname
        if (nickname != null) viewModel.event.onNicknameChange(nickname)
    }

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
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
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
                        .background(MooiTheme.colorScheme.backgroundDefault)
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(WindowInsets.navigationBars),
            ) {
                OnBoardingTitle(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    currentStep = 0,
                    title = stringResource(tutorialR.string.on_boarding_p0_title),
                    titleHighlights =
                        stringResource(tutorialR.string.on_boarding_p0_title_highlights).split(
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
                        label = stringResource(tutorialR.string.on_boarding_p0_name_label),
                        value = state.nickname,
                        onValueChange = event::onNicknameChange,
                        showCharCount = true,
                        maxCharCount = 8,
                        placeHolder = stringResource(tutorialR.string.on_boarding_p0_name_placeholder),
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
                            .imePadding()
                            .padding(horizontal = 15.dp)
                            .padding(
                                bottom = animatedPadding,
                            ),
                    labelString = stringResource(tutorialR.string.on_boarding_btn_next),
                    enabled = state.nicknameInputState == InputState.VALID,
                    onClick = {
                        focusManager.clearFocus()
                        onNicknameInputComplete(state.nickname)
                        navToGenderBirth()
                    },
                    isDefaultWidth = false,
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
            topDescription = stringResource(tutorialR.string.on_boarding_exit_modal_desc),
            title = stringResource(tutorialR.string.on_boarding_exit_modal_title),
            confirmLabel = stringResource(tutorialR.string.on_boarding_exit_modal_confirm),
            dismissLabel = stringResource(tutorialR.string.on_boarding_exit_modal_dismiss),
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
