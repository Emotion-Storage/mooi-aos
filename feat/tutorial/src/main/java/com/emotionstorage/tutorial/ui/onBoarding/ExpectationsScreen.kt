package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.Expectation
import com.emotionstorage.tutorial.R as tutorialR
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State
import com.emotionstorage.tutorial.ui.component.OnBoardingTitle
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground

/**
 * On boarding step 3
 * - select expectations
 */
@Composable
fun ExpectationsScreen(
    modifier: Modifier = Modifier,
    expectations: List<Expectation>? = null,
    viewModel: ExpectationsViewModel = hiltViewModel(),
    onExpectationsSelectComplete: (expectations: List<Expectation>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect("init") {
        if (expectations != null) {
            viewModel.onSelectExpectations(expectations)
        }
    }

    StatelessExpectationsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        onExpectationsSelectComplete = onExpectationsSelectComplete,
        navToAgreeTerms = navToAgreeTerms,
        navToBack = navToBack,
    )
}

@Composable
private fun StatelessExpectationsScreen(
    state: State,
    event: ExpectationsEvent,
    modifier: Modifier = Modifier,
    onExpectationsSelectComplete: (expectations: List<Expectation>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
    navToBack: () -> Unit = {},
) {
    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize(),
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize()
                    .padding(padding),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp),
            ) {
                OnBoardingTitle(
                    modifier = Modifier.fillMaxWidth(),
                    currentStep = 2,
                    title = stringResource(tutorialR.string.on_boarding_p2_title),
                    titleHighlights =
                        stringResource(tutorialR.string.on_boarding_p2_title_highlights).split(
                            ',',
                        ),
                )

                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(top = 30.dp)
                            .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        modifier = Modifier.height(24.dp),
                        style = MooiTheme.typography.body7,
                        color = Color.White,
                        text = stringResource(tutorialR.string.on_boarding_p2_input_title),
                    )
                    Text(
                        style =
                            MooiTheme.typography.body8.copy(
                                fontWeight = FontWeight.Light,
                            ),
                        color = MooiTheme.colorScheme.primaryBlue500,
                        text = stringResource(tutorialR.string.on_boarding_p2_input_desc),
                    )
                    Column(
                        modifier = modifier.padding(top = 20.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        state.expectations.forEachIndexed { index, expectation ->
                            ExpectationItem(
                                expectation = expectation,
                                isSelected = state.selectedExpectations.contains(expectation),
                                onClick = { event.onToggleExpectation(index) },
                            )
                        }
                    }
                }
            }

            CtaButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 15.dp)
                        .padding(bottom = 39.dp),
                labelString = stringResource(tutorialR.string.on_boarding_btn_next),
                enabled = state.isNextButtonEnabled,
                onClick = {
                    onExpectationsSelectComplete(state.selectedExpectations)
                    navToAgreeTerms()
                },
                isDefaultWidth = false,
            )
        }
    }
}

@Composable
private fun ExpectationItem(
    expectation: Expectation,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(72.dp)
                .subBackground(isSelected, defaultBackground = Color.Black)
                .clickable(
                    onClick = onClick,
                ).padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier =
                    Modifier
                        .width(21.dp)
                        .height(20.dp),
                painter =
                    painterResource(
                        when (expectation) {
                            Expectation.EMOTION -> tutorialR.drawable.expectation_0
                            Expectation.STRESS -> tutorialR.drawable.expectation_1
                            Expectation.REGRET -> tutorialR.drawable.expectation_2
                            Expectation.MEMORY -> tutorialR.drawable.expectation_3
                            Expectation.PATTERN -> tutorialR.drawable.expectation_4
                            Expectation.RECORD -> tutorialR.drawable.expectation_5
                        },
                    ),
                contentDescription = null,
            )

            Text(
                style = MooiTheme.typography.body8,
                color = if (isSelected) MooiTheme.colorScheme.primaryBlue500 else Color.White,
                text = expectation.content,
            )
        }

        if (isSelected) {
            Image(
                modifier =
                    Modifier
                        .width(20.dp)
                        .height(20.dp),
                painter = painterResource(tutorialR.drawable.success_fill),
                contentDescription = null,
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
            event =
                object : ExpectationsEvent {
                    override fun onToggleExpectation(index: Int) {}

                    override fun onSelectExpectations(expectations: List<Expectation>) {}
                },
        )
    }
}
