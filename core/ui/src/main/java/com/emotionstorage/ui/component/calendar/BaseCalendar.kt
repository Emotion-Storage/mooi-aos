package com.emotionstorage.ui.component.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.getWeekDatesOfTargetMonth
import com.emotionstorage.ui.R
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
    showYearMonthDropDownIcon: Boolean = false,
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
    }

) {
    Column(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background),
    ) {
        // year & month selection
        CalendarYearDateBox(
            calendarYearMonth = calendarYearMonth,
            minYearMonth = minYearMonth,
            maxYearMonth = maxYearMonth,
            onCalendarYearMonthSelect = onCalendarYearMonthSelect,
            showYearMonthDropDownIcon = showYearMonthDropDownIcon,
            onYearMonthDropDownIconClick = onYearMonthDropDownIconClick,
            calendarYearMonthTextStyle = calendarYearMonthTextStyle,
        )

        // calendar dates
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(
                items = listOf("일", "월", "화", "수", "목", "금", "토"),
                key = { it },
            ) { label ->
                weekDateItem(Modifier.weight(1f), label)
            }

            items(
                items = calendarYearMonth.getWeekDatesOfTargetMonth(),
                key = { it.toString() },
            ) { date ->
                dateItem(Modifier.weight(1f), date)
            }
        }
    }
}

@Composable
private fun CalendarYearDateBox(
    modifier: Modifier = Modifier,
    calendarYearMonth: YearMonth = YearMonth.now(),
    minYearMonth: YearMonth = YearMonth.now(),
    maxYearMonth: YearMonth = YearMonth.now(),
    onCalendarYearMonthSelect: (yearMonth: YearMonth) -> Unit = {},
    showYearMonthDropDownIcon: Boolean = false,
    onYearMonthDropDownIconClick: () -> Unit = {},
    calendarYearMonthTextStyle: TextStyle = MooiTheme.typography.mainButton,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp),
    ) {
        if (YearMonth.from(minYearMonth) < calendarYearMonth) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .size(width = 8.dp, height = 14.dp)
                        .clickable {
                            onCalendarYearMonthSelect(calendarYearMonth.minusMonths(1))
                        },
                painter = painterResource(id = R.drawable.arrow_back),
                colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                contentDescription = "",
            )
        }

        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .clickable(
                        enabled = showYearMonthDropDownIcon,
                        onClick = onYearMonthDropDownIconClick,
                    ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "${calendarYearMonth.year}년 ${calendarYearMonth.monthValue}월",
                style = calendarYearMonthTextStyle,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            if (showYearMonthDropDownIcon) {
                Image(
                    modifier = Modifier.size(10.dp, 9.dp),
                    painter = painterResource(id = R.drawable.toggle_down),
                    contentDescription = "calendar year month picker",
                )
            }
        }

        if (calendarYearMonth < maxYearMonth) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.CenterEnd)
                        .size(width = 8.dp, height = 14.dp)
                        .rotate(180f)
                        .clickable {
                            onCalendarYearMonthSelect(calendarYearMonth.plusMonths(1))
                        },
                painter = painterResource(id = R.drawable.arrow_back),
                colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                contentDescription = "",
            )
        }
    }
}

@Preview
@Composable
private fun BaseCalendarPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }

    MooiTheme {
        BaseCalendar(
            calendarYearMonth = calendarYearMonth,
            minYearMonth = YearMonth.now().minusYears(1),
            maxYearMonth = YearMonth.now().plusYears(1),
            onCalendarYearMonthSelect = setCalendarYearMonth,
        )
    }
}
