package com.emotionstorage.ui.component.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.calendar.SwipeCalendar
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

private val CALENDER_MIN_DATE = LocalDate.of(1970, 1, 1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    selectedDate: LocalDate? = null,
    onDateSelect: (LocalDate) -> Unit = {},
    calendarYearMonth: YearMonth = YearMonth.now(),
    onYearMonthSelect: (YearMonth) -> Unit = {},
    onYearMonthDropdownClick: () -> Unit = {},
    minDate: LocalDate = CALENDER_MIN_DATE,
    maxDate: LocalDate = LocalDate.now().plusYears(1),
) {
    BottomSheet(
        hideDragHandle = true,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
    ) {
        val coroutineScope = rememberCoroutineScope()

        SwipeCalendar(
            modifier =
                modifier
                    .fillMaxWidth()
                    // fixed calendar height
                    .heightIn(min = 353.dp)
                    .padding(top = 20.dp, bottom = 35.dp),
            calendarYearMonth = calendarYearMonth,
            minYearMonth = YearMonth.from(minDate),
            maxYearMonth = YearMonth.from(maxDate),
            onCalendarYearMonthSelect = onYearMonthSelect,
            showDropDownIcon = true,
            onDropDownIconClick = onYearMonthDropdownClick,
            headerTextStyle = MooiTheme.typography.body7.copy(color = Color.White),
            weekDateItem = { modifier, label ->
                Text(
                    modifier = modifier.padding(top = 17.dp, bottom = 14.dp),
                    text = label,
                    style = MooiTheme.typography.caption6,
                    color = MooiTheme.colorScheme.gray400,
                    textAlign = TextAlign.Center,
                )
            },
            dateItem = { modifier, pageYearMonth, date ->
                DateItem(
                    date = date,
                    calendarYearMonth = pageYearMonth,
                    minDate = minDate,
                    maxDate = maxDate,
                    selectedDate = selectedDate,
                    modifier = modifier,
                    onClick = {
                        coroutineScope
                            .launch { sheetState.hide() }
                            .invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDateSelect(date)
                                }
                            }
                    },
                )
            },
        )
    }
}

@Composable
private fun DateItem(
    date: LocalDate,
    calendarYearMonth: YearMonth,
    minDate: LocalDate,
    maxDate: LocalDate,
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.padding(vertical = 8.dp),
    ) {
        if (date.year == calendarYearMonth.year && date.month == calendarYearMonth.month) {
            val isValidDate = (!date.isBefore(minDate) && !date.isAfter(maxDate))

            Box(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(29.dp)
                        .offset(y = -0.5.dp)
                        .background(
                            if (selectedDate != date) {
                                Color.Transparent
                            } else {
                                MooiTheme.colorScheme.secondary
                            },
                            CircleShape,
                        ).clip(CircleShape)
                        .clickable(
                            enabled = isValidDate,
                            onClick = onClick,
                        ),
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = date.dayOfMonth.toString(),
                style = MooiTheme.typography.body7,
                color =
                    Color.White.copy(
                        alpha = if (isValidDate) 1f else 0.13f,
                    ),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun DatePickerBottomSheetPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) {
            DatePickerBottomSheet(
                // open sheet state for preview
                sheetState =
                    rememberStandardBottomSheetState(
                        initialValue = SheetValue.Expanded,
                    ),
                selectedDate = LocalDate.of(2025, 11, 3),
                minDate = LocalDate.now().minusDays(7),
                maxDate = LocalDate.now().plusDays(10),
            )
        }
    }
}
