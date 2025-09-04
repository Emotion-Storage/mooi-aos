package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.domain.model.Expectation
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground

/**
 * On boarding step 3
 * - select expectations
 */
@Composable
fun ExpectationsScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpectationsViewModel = hiltViewModel(),
    onExpectationsSelectComplete: (expectations: List<Expectation>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
    navToBack: () -> Unit = {}
) {
    val state = viewModel.state.collectAsState().value

    StatelessExpectationsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        onExpectationsSelectComplete = onExpectationsSelectComplete,
        navToAgreeTerms = navToAgreeTerms,
        navToBack = navToBack
    )
}

@Composable
private fun StatelessExpectationsScreen(
    state: State,
    event: ExpectationsEvent,
    modifier: Modifier = Modifier,
    onExpectationsSelectComplete: (expectations: List<Expectation>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
    navToBack: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = { TopAppBar(showBackButton = true, onBackClick = navToBack) }
    ) { padding ->
        Box(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                OnBoardingTitle(
                    modifier = Modifier.fillMaxWidth(),
                    currentStep = 2,
                    title = stringResource(R.string.on_boarding_expectations_title),
                    titleHighlights = stringResource(R.string.on_boarding_expectations_title_highlights).split(
                        ','
                    )
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 30.dp)
                        .verticalScroll(rememberScrollState()),
                ) {
                    Text(
                        modifier = Modifier.height(24.dp),
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = Color.White,
                        text = "감정 기록 목적",
                    )
                    Text(
                        style = MooiTheme.typography.body3.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light
                        ),
                        color = MooiTheme.colorScheme.primary,
                        text = "* 여러 개를 선택할 수도 있어요",
                    )
                    Column(
                        modifier = modifier.padding(top = 20.dp, bottom = 120.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.expectations.forEachIndexed { index, expectation ->
                            ExpectationItem(
                                expectation = expectation,
                                isSelected = state.selectedExpectations.contains(expectation),
                                onClick = { event.onToggleExpectation(index) }
                            )
                        }
                    }
                }
            }

            CtaButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = state.isNextButtonEnabled,
                onClick = {
                    onExpectationsSelectComplete(state.selectedExpectations)
                    navToAgreeTerms()
                }
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
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .subBackground(isSelected, defaultBackground = Color.Black)
            .clickable(
                onClick = onClick
            )
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(21.dp)
                    .height(20.dp),
                painter = painterResource(
                    when (expectation) {
                        Expectation.EMOTION -> R.drawable.expectation_0
                        Expectation.STRESS -> R.drawable.expectation_1
                        Expectation.REGRET -> R.drawable.expectation_2
                        Expectation.MEMORY -> R.drawable.expectation_3
                        Expectation.PATTERN -> R.drawable.expectation_4
                        Expectation.RECORD -> R.drawable.expectation_5
                    }
                ),
                contentDescription = null
            )

            Text(
                style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                color = if (isSelected) MooiTheme.colorScheme.primary else Color.White,
                text = expectation.content,
            )
        }

        if (isSelected) {
            Image(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp),
                painter = painterResource(R.drawable.success_fill),
                contentDescription = null
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
