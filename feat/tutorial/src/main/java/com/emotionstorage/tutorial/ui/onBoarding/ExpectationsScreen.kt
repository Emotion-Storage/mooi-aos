package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding step 3
 * - select expectations
 */
@Composable
fun ExpectationsScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpectationsViewModel = hiltViewModel(),
    navToAgreeTerms: (expectations: List<String>) -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    StatelessExpectationsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        navToAgreeTerms = navToAgreeTerms
    )
}

@Composable
private fun StatelessExpectationsScreen(
    state: State,
    event: ExpectationsEvent,
    modifier: Modifier = Modifier,
    navToAgreeTerms: (expectations: List<String>) -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = { TopAppBar(showBackButton = true) }
    ) { padding ->
        Column(
            modifier = modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
        ) {
            OnBoardingTitle(
                modifier = Modifier.fillMaxWidth(),
                currentStep = 1,
                title = stringResource(R.string.on_boarding_expectations_title),
                titleHighlights = stringResource(R.string.on_boarding_expectations_title_highlights).split(
                    ','
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(37.dp)
            ) {
                // todo: add expectations list
            }

            // todo: float button
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = state.isNextButtonEnabled,
                onClick = {
                    navToAgreeTerms(state.expectations.map { it.content })
                }
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun ExpectationsScreenPreview() {
    MooiTheme {
        StatelessExpectationsScreen(
            state = State(),
            event = object : ExpectationsEvent {
                override fun onToggleExpectation(index: Int) {}
            },
        )
    }
}
