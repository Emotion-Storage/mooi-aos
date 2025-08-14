package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.onBoarding.InputNicknameEvent
import com.emotionstorage.tutorial.presentation.onBoarding.NicknameViewModel.State.InputState
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.component.TextInputState
import com.emotionstorage.ui.component.TopAppBar

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
) {
    StatelessNicknameScreen(
        modifier = modifier,
        state = viewModel.state.collectAsState().value,
        event = viewModel.event,
        onNicknameInputComplete = onNicknameInputComplete,
        navToGenderBirth = navToGenderBirth
    )
}

@Composable
private fun StatelessNicknameScreen(
    modifier: Modifier = Modifier,
    state: NicknameViewModel.State,
    event: InputNicknameEvent,
    onNicknameInputComplete: (nickname: String) -> Unit = {},
    navToGenderBirth: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            TopAppBar(showBackButton = true)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding(),
        ) {
            OnBoardingTitle(
                modifier = Modifier.fillMaxWidth(),
                currentStep = 0,
                title = stringResource(R.string.on_boarding_nickname_title),
                titleHighlights = stringResource(R.string.on_boarding_nickname_title_highlights).split(
                    ','
                )
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 30.dp)
            ) {
                TextInput(
                    label = "이름",
                    value = state.nickname,
                    onValueChange = event::onNicknameChange,
                    showCharCount = true,
                    maxCharCount = 8,
                    state = when (state.nicknameInputState) {
                        InputState.EMPTY -> TextInputState.Empty(infoMessage = state.nicknameHelperMessage)
                        InputState.INVALID -> TextInputState.Error(errorMessage = state.nicknameHelperMessage)
                        InputState.VALID -> TextInputState.Success(successMessage = state.nicknameHelperMessage)
                    } as TextInputState
                )
            }

            // todo: add bottom padding when keyboard is hidden
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = state.nicknameInputState == InputState.VALID,
                onClick = {
                    onNicknameInputComplete(state.nickname)
                    navToGenderBirth()
                }
            )
        }
    }
}


@PreviewScreenSizes
@Composable
private fun NicknameScreenPreview() {
    MooiTheme {
        StatelessNicknameScreen(
            state = NicknameViewModel.State(),
            event = object : InputNicknameEvent {
                override fun onNicknameChange(nickname: String) {}
            }
        )
    }
}
