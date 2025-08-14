package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.auth.domain.model.SignupForm.GENDER
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthEvent
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthViewModel.State
import com.emotionstorage.ui.component.ScrollPicker
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.R
import com.emotionstorage.ui.component.CtaButton
import java.time.LocalDate

/**
 * On boarding step 2
 * - input user gender
 * - input user birth date
 */
@Composable
fun GenderBirthScreen(
    modifier: Modifier = Modifier,
    viewModel: GenderBirthViewModel = hiltViewModel(),
    nickname: String = "",
    onGenderBirthInputComplete: (gender: GENDER, birth: LocalDate) -> Unit = { _, _ -> },
    navToExpectations: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    StatelessGenderBirthScreen(
        event = viewModel.event,
        state = state,
        modifier = modifier,
        nickname = nickname,
        onGenderBirthInputComplete = onGenderBirthInputComplete,
        navToExpectations = navToExpectations
    )
}

@Composable
private fun StatelessGenderBirthScreen(
    event: GenderBirthEvent,
    state: State,
    modifier: Modifier = Modifier,
    nickname: String = "",
    onGenderBirthInputComplete: (gender: GENDER, birth: LocalDate) -> Unit = { _, _ -> },
    navToExpectations: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize(),
        topBar = { TopAppBar(showBackButton = true) }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .imePadding(),
        ) {
            OnBoardingTitle(
                modifier = Modifier.fillMaxWidth(),
                currentStep = 1,
                title = stringResource(
                    if (nickname.length < 5) R.string.on_boarding_gender_birth_title_short else R.string.on_boarding_gender_birth_title_long,
                    nickname
                ),
                titleHighlights = stringResource(R.string.on_boarding_gender_birth_title_highlights).split(
                    ','
                )
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(37.dp)
            ) {
                GenderInput(
                    selectedGender = state.gender,
                    onGenderSelect = event::onGenderSelect,
                    modifier = Modifier.fillMaxWidth()
                )

                BirthInput(
                    selectedYear = state.yearPickerState.selectedValue,
                    onYearSelect = event::onYearPickerSelect,
                    yearRange = state.yearPickerState.range,
                    yearEnabled = state.yearPickerState.enabled,

                    selectedMonth = state.monthPickerState.selectedValue,
                    onMonthSelect = event::onMonthPickerSelect,
                    monthRange = state.monthPickerState.range,
                    monthEnabled = state.monthPickerState.enabled,

                    selectedDay = state.dayPickerState.selectedValue,
                    onDaySelect = event::onDayPickerSelect,
                    dayRange = state.dayPickerState.range,
                    dayEnabled = state.dayPickerState.enabled,
                )
            }

            // todo: add bottom padding when keyboard is hidden
            CtaButton(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(bottom = 39.dp),
                label = "다음으로",
                enabled = state.isNextButtonEnabled,
                onClick = {
                    if (
                        (state.gender == null) ||
                        (state.yearPickerState.selectedValue == null) ||
                        (state.monthPickerState.selectedValue == null) ||
                        (state.dayPickerState.selectedValue == null)
                    ) return@CtaButton

                    onGenderBirthInputComplete(
                        state.gender!!,
                        LocalDate.of(
                            state.yearPickerState.selectedValue.toInt(),
                            state.monthPickerState.selectedValue.toInt(),
                            state.dayPickerState.selectedValue.toInt()
                        )
                    )
                    navToExpectations()
                }
            )
        }
    }
}

@Composable
private fun GenderInput(
    modifier: Modifier = Modifier,
    selectedGender: GENDER? = null,
    onGenderSelect: (GENDER?) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
            color = Color.White,
            text = "성별",
            modifier = Modifier.height(24.dp)
        )
        Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            GENDER.entries.forEach {
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(94.94.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            // todo: add gradient bg & border when selected value exists
                            if (selectedGender == it) Color.Gray else Color.Black
                        )
                        .clickable {
                            if (selectedGender == it) onGenderSelect(null) else
                                onGenderSelect(it)
                        }
                        .padding(14.dp)
                ) {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = Color.White,
                        text = when (it) {
                            GENDER.MALE -> "남자"
                            GENDER.FEMALE -> "여자"
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun BirthInput(
    modifier: Modifier = Modifier,

    selectedYear: String? = null,
    onYearSelect: (String) -> Unit = {},
    yearRange: List<String> = emptyList(),
    yearEnabled: Boolean = true,

    selectedMonth: String? = null,
    onMonthSelect: (String) -> Unit = {},
    monthRange: List<String> = emptyList(),
    monthEnabled: Boolean = true,

    selectedDay: String? = null,
    onDaySelect: (String) -> Unit = {},
    dayRange: List<String> = emptyList(),
    dayEnabled: Boolean = true,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
            color = Color.White,
            text = "생년월일",
            modifier = Modifier.height(24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.Top
        ) {
            ScrollPicker(
                placeholder = "년도",
                selectedValue = selectedYear,
                onValueChange = onYearSelect,
                range = yearRange,
                enabled = yearEnabled,
                modifier = Modifier.width(110.dp)
            )
            ScrollPicker(
                placeholder = "월",
                selectedValue = selectedMonth,
                onValueChange = onMonthSelect,
                range = monthRange,
                enabled = monthEnabled,
                modifier = Modifier.width(78.dp)
            )
            ScrollPicker(
                placeholder = "일",
                selectedValue = selectedDay,
                onValueChange = onDaySelect,
                range = dayRange,
                enabled = dayEnabled,
                modifier = Modifier.width(78.dp)
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun GenderBirthScreenPreview() {
    MooiTheme {
        StatelessGenderBirthScreen(
            event = object : GenderBirthEvent {
                override fun onGenderSelect(gender: GENDER?) {}

                override fun onYearPickerSelect(year: String) {}

                override fun onMonthPickerSelect(month: String) {}

                override fun onDayPickerSelect(day: String) {}
            },
            state = State(),
            nickname = "찡찡이",
        )
    }
}

