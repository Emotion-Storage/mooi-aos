package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.SignupCompleteAction
import com.emotionstorage.tutorial.presentation.SignupCompleteSideEffect
import com.emotionstorage.tutorial.presentation.SignupCompleteViewModel
import com.emotionstorage.tutorial.ui.onBoarding.OnBoardingTitle
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
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    viewModel: SignupCompleteViewModel = hiltViewModel(),
    navToHome: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.container.sideEffectFlow.collect {effect ->
            when (effect) {
                SignupCompleteSideEffect.LoginSuccess -> {
                    navToHome()
                }

                SignupCompleteSideEffect.LoginFailed -> {
                    // todo: handle login failure
                }
            }
        }
    }

    StatelessSignupCompleteScreen(
        modifier = modifier,
        onLogin = {
            viewModel.onAction(SignupCompleteAction.Login(provider, idToken))
        }
    )
}

@Composable
private fun StatelessSignupCompleteScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit = {},
) {
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

            // todo: 버튼 위 말풍선 추가하기
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 39.dp),
                label = "메인화면으로 이동",
                onClick = onLogin
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun SignupCompleteScreenPreview() {
    MooiTheme {
        StatelessSignupCompleteScreen()
    }
}