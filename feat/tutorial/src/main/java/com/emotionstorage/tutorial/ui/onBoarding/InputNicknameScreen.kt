package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.InputNicknameViewModel
import com.emotionstorage.tutorial.presentation.OnBoardingViewModel
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.R
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.component.TextInputState

/**
 * On boarding step 1
 * - input user nickname
 */
@Composable
fun InputNicknameScreen(
    modifier: Modifier = Modifier,
//    onBoardingViewModel: OnBoardingViewModel = hiltViewModel(),
    inputNicknameViewModel: InputNicknameViewModel = hiltViewModel(),
    navToInputGenderBirth: () -> Unit = {},
) {
    // todo: add top bar with nav back icon
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
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

            // todo: get text input state from viewmodel
            Box(modifier = Modifier
                .weight(1f)
                .padding(vertical = 30.dp)) {
                TextInput(
                    label = "이름",
                    value = "찡찡이",
                    onValueChange = {},
                    showCharCount = true,
                    maxCharCount = 8,
                    state = TextInputState.Success("사용 가능한 이름입니다.")
                )
            }

            // todo: enable when nickname is valid
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = true,
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
        InputNicknameScreen()
    }
}