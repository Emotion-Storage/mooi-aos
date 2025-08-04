package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.InputNicknameViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.InputNicknameEvent
import com.emotionstorage.tutorial.presentation.InputNicknameViewModel.State.InputState
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.component.TextInputState
import com.emotionstorage.ui.component.TopAppBar

/**
 * On boarding step 1
 * - input user nickname
 */
@Composable
fun InputNicknameScreen(
    modifier: Modifier = Modifier,
    viewModel: InputNicknameViewModel = hiltViewModel(),
    navToInputGenderBirth: () -> Unit = {},
) {
    StatelessInputNicknameScreen(
        modifier = modifier,
        state = viewModel.state.collectAsState().value,
        event = viewModel.event,
        navToInputGenderBirth = navToInputGenderBirth
    )
}

@Composable
private fun StatelessInputNicknameScreen(
    modifier: Modifier = Modifier,
    state: InputNicknameViewModel.State,
    event: InputNicknameEvent,
    navToInputGenderBirth: () -> Unit = {},
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
            modifier = modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
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

            CtaButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = state.nicknameInputState == InputState.VALID,
                onClick = navToInputGenderBirth
            )
        }
    }
}


//@PreviewScreenSizes
@Preview
@Composable
private fun InputNicknameScreenPreview() {
    MooiTheme {
        StatelessInputNicknameScreen(
            state = InputNicknameViewModel.State(),
            event = object : InputNicknameEvent {
                override fun onNicknameChange(nickname: String) {}
            }
        )
    }
}