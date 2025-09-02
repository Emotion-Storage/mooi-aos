package com.emotionstorage.tutorial.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.SplashViewModel
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    navToTutorial: () -> Unit = {},
    navToHome: () -> Unit = {},
) {

    val state = viewModel.state.collectAsState()
    StatelessSplashScreen(
        state = state.value,
        modifier = modifier,
        navToTutorial = navToTutorial,
        navToHome = navToHome,
    )
}

@Composable
private fun StatelessSplashScreen(
    state: SplashViewModel.State,
    modifier: Modifier = Modifier,
    navToTutorial: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    LaunchedEffect(state) {
        if (state.splashState != SplashViewModel.State.SplashState.Done) return@LaunchedEffect

        when (state.autoLoginState) {
            is SplashViewModel.State.AutoLoginState.Loading -> {
                // do nothing
            }

            is SplashViewModel.State.AutoLoginState.Success -> {
                navToHome()
            }

            is SplashViewModel.State.AutoLoginState.Failed -> {
                navToTutorial()
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(148.dp)
                    .offset(y = 214.dp)
                    .background(Color.Black)
            )
        }
    }
}


@Preview
@Composable
private fun SplashScreenPreview() {
    MooiTheme {
        StatelessSplashScreen(
            state = SplashViewModel.State(),
        )
    }
}