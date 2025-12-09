package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.calendar.SwipeCalendar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.YearMonth

private val CALENDAR_MIN_YEAR_MONTH = YearMonth.of(1970, 1)

private object TimeCapsuleCalendarDesignToken {
    val calendarWidth = 330
    val dateWidth = 30
}

private val DUMMY_TIME_CAPSULE_DATES =
    (1..31)
        .toList()
        .filter {
            it % 3 == 0
        }.map {
            LocalDate.of(LocalDate.now().year, LocalDate.now().month, it)
        }

@Composable
fun TimeCapsuleCalendar(
    modifier: Modifier = Modifier,
    calendarYearMonth: YearMonth = YearMonth.now(),
    onCalendarYearMonthSelect: (yearMonth: YearMonth) -> Unit = {},
    onDropDownIconClick: () -> Unit = {},
    timeCapsuleDates: List<LocalDate> = emptyList(),
    onDateSelect: (LocalDate) -> Unit = {},
) {
    SwipeCalendar(
        modifier = modifier.width(TimeCapsuleCalendarDesignToken.calendarWidth.dp),
        calendarYearMonth = calendarYearMonth,
        minYearMonth = CALENDAR_MIN_YEAR_MONTH,
        maxYearMonth = YearMonth.now(),
        onCalendarYearMonthSelect = onCalendarYearMonthSelect,
        showDropDownIcon = true,
        onDropDownIconClick = onDropDownIconClick,
        weekDateItem = { modifier, label ->
            Text(
                modifier = modifier.padding(top = 17.dp, bottom = 16.dp),
                text = label,
                style = MooiTheme.typography.caption5,
                color = MooiTheme.colorScheme.gray400,
                textAlign = TextAlign.Center,
            )
        },
        dateItem = { modifier, pageYearMonth, date ->
            DateItem(
                modifier = modifier.padding(bottom = 6.dp),
                date = date,
                onDateClick = onDateSelect,
                isShown = date.year == pageYearMonth.year && date.month == pageYearMonth.month,
                isFilled = date in timeCapsuleDates,
                isToday = date == LocalDate.now(),
            )
        },
    )
}

@Composable
private fun DateItem(
    modifier: Modifier = Modifier,
    isShown: Boolean = true,
    date: LocalDate = LocalDate.now(),
    onDateClick: (date: LocalDate) -> Unit = {},
    isFilled: Boolean = false,
    isToday: Boolean = false,
) {
    Box(
        modifier =
            modifier
                .alpha(if (isShown) 1f else 0f)
                .clickable(
                    enabled = isShown && isFilled,
                    onClick = {
                        onDateClick(date)
                    },
                ),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .run {
                        if (isToday) {
                            this
                                .background(
                                    MooiTheme.brushScheme.gra2,
                                    RoundedCornerShape(20.dp),
                                )
                        } else {
                            this.background(Color.Transparent)
                        }
                    }.padding(horizontal = 5.dp)
                    .padding(top = 5.dp, bottom = 7.dp),
            verticalArrangement =
                Arrangement.spacedBy(
                    9.dp,
                    alignment = Alignment.CenterVertically,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.height(14.dp),
                text = date.dayOfMonth.toString(),
                style = MooiTheme.typography.caption6,
                color = Color.White,
            )
            Image(
                modifier =
                    Modifier
                        .size(TimeCapsuleCalendarDesignToken.dateWidth.dp),
                painter =
                    painterResource(
                        if (isFilled) R.drawable.ic_star_filled else R.drawable.ic_star,
                    ),
                contentDescription = if (isFilled) "filled date icon" else "empty date icon",
            )
        }
    }
}

@Preview
@Composable
private fun DateItemPreview() {
    MooiTheme {
        Row(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DateItem(
                date = LocalDate.now(),
                isFilled = true,
                isToday = true,
            )
            DateItem(
                date = LocalDate.now(),
                isFilled = false,
                isToday = true,
            )
            DateItem(
                date = LocalDate.now(),
                isFilled = true,
                isToday = false,
            )
            DateItem(
                date = LocalDate.now(),
                isFilled = false,
                isToday = false,
            )
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleCalendarPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(horizontal = 16.dp, vertical = 30.dp),
        ) {
            TimeCapsuleCalendar(
                modifier = Modifier.align(Alignment.TopCenter),
                calendarYearMonth = calendarYearMonth,
                onCalendarYearMonthSelect = setCalendarYearMonth,
                timeCapsuleDates = DUMMY_TIME_CAPSULE_DATES,
            )
        }
    }
}
