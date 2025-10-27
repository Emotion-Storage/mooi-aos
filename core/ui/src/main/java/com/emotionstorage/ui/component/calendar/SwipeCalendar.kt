package com.emotionstorage.ui.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Composable
fun SwipeCalendar(
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
    },
) {
    val totalMonths = ChronoUnit.MONTHS.between(minYearMonth, maxYearMonth).toInt() + 1
    val initialPageIndex = ChronoUnit.MONTHS.between(minYearMonth, calendarYearMonth).toInt()
    val pagerState = rememberPagerState(initialPage = initialPageIndex, pageCount = { totalMonths })

    val currentYearMonth by remember {
        derivedStateOf {
            minYearMonth.plusMonths(pagerState.currentPage.toLong())
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        // update calendar year month on page change
        onCalendarYearMonthSelect(currentYearMonth)
    }

    val coroutineScope = rememberCoroutineScope()
    Column(modifier = modifier) {
        CalendarYearMonthIndicator(
            calendarYearMonth = currentYearMonth,
            minYearMonth = minYearMonth,
            maxYearMonth = maxYearMonth,
            onCalendarYearMonthSelect = {
                coroutineScope.launch {
                    val targetIndex = ChronoUnit.MONTHS.between(minYearMonth, it).toInt()
                    pagerState.animateScrollToPage(targetIndex.coerceIn(0, totalMonths - 1))
                }
            },
            showYearMonthDropDownIcon = showYearMonthDropDownIcon,
            onYearMonthDropDownIconClick = onYearMonthDropDownIconClick,
            calendarYearMonthTextStyle = calendarYearMonthTextStyle,
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                weekDateItem(Modifier.weight(1f), it)
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val yearMonth = minYearMonth.plusMonths(page.toLong())
            BaseCalendar(
                minYearMonth = minYearMonth,
                maxYearMonth = maxYearMonth,
                calendarYearMonth = yearMonth,
                onCalendarYearMonthSelect = onCalendarYearMonthSelect,
                showYearMonthIndicator = false,
                showWeekDates = false,
                dateItem = dateItem
            )
        }
    }
}


@Preview
@Composable
private fun SwipeCalendarPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }

    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 30.dp),
        ) {
            SwipeCalendar(
                modifier = Modifier.align(Alignment.TopCenter),
                calendarYearMonth = calendarYearMonth,
                minYearMonth = YearMonth.now().minusYears(1),
                maxYearMonth = YearMonth.now().plusYears(1),
                onCalendarYearMonthSelect = setCalendarYearMonth,
            )
        }
    }
}
