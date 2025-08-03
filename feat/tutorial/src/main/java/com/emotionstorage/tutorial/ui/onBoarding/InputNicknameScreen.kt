package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.InputNicknameViewModel
import com.emotionstorage.tutorial.presentation.OnBoardingViewModel
import com.emotionstorage.ui.theme.MooiTheme

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
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            Text("Input NickName")
        }
    }
}

@PreviewScreenSizes
@Composable
private fun InputNicknameScreenPreview() {
    InputNicknameScreen()
}