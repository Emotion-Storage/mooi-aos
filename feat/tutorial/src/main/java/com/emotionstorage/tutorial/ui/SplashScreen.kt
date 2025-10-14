package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.SplashAction
import com.emotionstorage.tutorial.presentation.SplashSideEffect
import com.emotionstorage.tutorial.presentation.SplashViewModel
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    navToTutorial: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.onAction(SplashAction.Initiate)

        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is SplashSideEffect.AutoLoginFailed -> navToTutorial()
                is SplashSideEffect.AutoLoginSuccess -> navToHome()
            }
        }
    }

    StatelessSplashScreen(modifier = modifier)
}

@Composable
private fun StatelessSplashScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize(),
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(padding),
        ) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 215.dp)
                        .width(235.dp),
                painter = painterResource(R.drawable.splash_graphic),
                contentDescription = "Mooi",
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MooiTheme {
        StatelessSplashScreen()
    }
}
