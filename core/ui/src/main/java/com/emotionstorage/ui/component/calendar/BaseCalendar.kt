package com.emotionstorage.ui.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun BaseCalendar(
    minYearMonth: YearMonth,
    maxYearMonth: YearMonth,
    modifier: Modifier = Modifier,
    calendarYearMonth: YearMonth = YearMonth.now(),
    onCalendarYearMonthSelect: (yearMonth: YearMonth) -> Unit = {},
    showYearMonthDropDownIcon: Boolean = true,
    onYearMonthDropDownIconClick: () -> Unit = {},
    calendarYearMonthTextStyle: TextStyle = MooiTheme.typography.mainButton,
    weekDateItem: @Composable (modifier: Modifier, label: String) -> Unit = { modifier, label ->
        Text(
            modifier = modifier.padding(vertical = 16.dp),
            text = label,
            style = MooiTheme.typography.caption5,
            color = MooiTheme.colorScheme.gray400,
            textAlign = TextAlign.Center,
        )
    },
    dateItem: @Composable (modifier: Modifier, date: LocalDate) -> Unit = { modifier, date ->
        Text(
            modifier = modifier.padding(bottom = 6.dp),
            text = date.dayOfMonth.toString(),
            style = MooiTheme.typography.caption5,
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    },
) {
    Column(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background),
    ) {
        CalendarYearMonthSelector(
            calendarYearMonth = calendarYearMonth,
            minYearMonth = minYearMonth,
            maxYearMonth = maxYearMonth,
            onCalendarYearMonthSelect = onCalendarYearMonthSelect,
            showYearMonthDropDownIcon = showYearMonthDropDownIcon,
            onYearMonthDropDownIconClick = onYearMonthDropDownIconClick,
            calendarYearMonthTextStyle = calendarYearMonthTextStyle,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                weekDateItem(Modifier.weight(1f), it)
            }
        }

        CalendarDates(
            calendarYearMonth = calendarYearMonth,
            dateItem = dateItem,
        )
    }
}

@Preview
@Composable
private fun BaseCalendarPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }

    MooiTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            BaseCalendar(
                calendarYearMonth = calendarYearMonth,
                minYearMonth = YearMonth.now().minusYears(1),
                maxYearMonth = YearMonth.now().plusYears(1),
                onCalendarYearMonthSelect = setCalendarYearMonth,
            )

            BaseCalendar(
                calendarYearMonth = calendarYearMonth,
                minYearMonth = YearMonth.now().minusYears(1),
                maxYearMonth = YearMonth.now().plusYears(1),
                showYearMonthDropDownIcon = false,
            )
        }
    }
}
