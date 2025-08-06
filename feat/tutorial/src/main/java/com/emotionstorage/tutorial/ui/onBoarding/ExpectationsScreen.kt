package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.emotionstorage.tutorial.R
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsEvent
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State.Expectation
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
    onExpectationsSelectComplete: (expectations: List<String>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    StatelessExpectationsScreen(
        state = state,
        event = viewModel.event,
        modifier = modifier,
        onExpectationsSelectComplete = onExpectationsSelectComplete,
        navToAgreeTerms = navToAgreeTerms
    )
}

@Composable
private fun StatelessExpectationsScreen(
    state: State,
    event: ExpectationsEvent,
    modifier: Modifier = Modifier,
    onExpectationsSelectComplete: (expectations: List<String>) -> Unit = {},
    navToAgreeTerms: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = { TopAppBar(showBackButton = true) }
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
                                index = index,
                                expectation = expectation,
                                isSelected = expectation.isSelected,
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
                    onExpectationsSelectComplete(state.expectations.map { it.content })
                    navToAgreeTerms()
                }
            )
        }
    }
}

@Composable
private fun ExpectationItem(
    index: Int,
    expectation: Expectation,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                // todo: add gradient bg & border if is selected
                if (isSelected) Color.Gray else Color.Black
            )
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
                    when (index) {
                        0 -> R.drawable.expectation_0
                        1 -> R.drawable.expectation_1
                        2 -> R.drawable.expectation_2
                        3 -> R.drawable.expectation_3
                        4 -> R.drawable.expectation_4
                        else -> R.drawable.expectation_5
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
            state = State(
                expectations = listOf(
                    Expectation(content = "내 감정을 정리하고 싶어요", isSelected = false),
                    Expectation(content = "스트레스를 관리하고 싶어요", isSelected = false),
                    Expectation(content = "후회나 힘든 감정을 털어내고 싶어요", isSelected = false),
                    Expectation(content = "좋은 기억을 오래 간직하고 싶어요", isSelected = false),
                )
            ),
            event = object : ExpectationsEvent {
                override fun onToggleExpectation(index: Int) {}
            },
        )
    }
}
