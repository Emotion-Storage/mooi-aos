package com.emotionstorage.tutorial.ui.onBoarding

import SpeechBubble
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.emotionstorage.tutorial.R
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch

/**
 * On boarding step 5
 * - login & navigate to main
 */
@Composable
fun SignupCompleteScreen(
    modifier: Modifier = Modifier,
    onLogin: suspend () -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = {
            TopAppBar()
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            OnBoardingTitle(
                modifier = Modifier.fillMaxWidth(),
                showSteps = false,
                title = stringResource(R.string.on_boarding_signup_complete_title),
                titleHighlights = stringResource(R.string.on_boarding_signup_complete_title_highlights).split(
                    ','
                )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SpeechBubble(
                    text = "비밀은 지켜드릴게요,\n당신의 감정을 편하게 나누어보세요.",
                )

                CtaButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 39.dp),
                    label = "메인화면으로 이동",
                    onClick = {
                        coroutineScope.launch {
                            onLogin()
                        }
                    }
                )
            }
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SignupCompleteScreenPreview() {
    MooiTheme {
        SignupCompleteScreen()
    }
}