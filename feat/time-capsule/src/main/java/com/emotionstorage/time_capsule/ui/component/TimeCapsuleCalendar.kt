package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.calendar.BaseCalendar
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
    onYearMonthDropDownIconClick: () -> Unit = {},
    timeCapsuleDates: List<LocalDate> = emptyList(),
    onDateSelect: (LocalDate) -> Unit = {},
) {
    BaseCalendar(
        modifier =
            modifier
                .width(TimeCapsuleCalendarDesignToken.calendarWidth.dp),
        calendarYearMonth = calendarYearMonth,
        minYearMonth = CALENDAR_MIN_YEAR_MONTH,
        maxYearMonth = YearMonth.now(),
        onCalendarYearMonthSelect = onCalendarYearMonthSelect,
        showYearMonthDropDownIcon = true,
        onYearMonthDropDownIconClick = onYearMonthDropDownIconClick,
        weekDateItem = { modifier, label ->
            Text(
                modifier = modifier.padding(top = 17.dp, bottom = 16.dp),
                text = label,
                style = MooiTheme.typography.caption5,
                color = MooiTheme.colorScheme.gray400,
                textAlign = TextAlign.Center,
            )
        },
        dateItem = { modifier, date ->
            DateItem(
                modifier = modifier.padding(bottom = 6.dp),
                date = date,
                onClick = onDateSelect,
                isShown = date.year == calendarYearMonth.year && date.month == calendarYearMonth.month,
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
    onClick: (date: LocalDate) -> Unit = {},
    isFilled: Boolean = false,
    isToday: Boolean = false,
) {
    if (!isShown) {
        Spacer(
            modifier.fillMaxWidth(),
        )
    } else {
        Box(modifier = modifier) {
            Column(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .background(
                            if (isToday) MooiTheme.colorScheme.secondary else Color.Transparent,
                            shape = RoundedCornerShape(20.dp),
                        ).clickable { onClick(date) }
                        .padding(horizontal = 3.5.dp)
                        .padding(top = 4.dp, bottom = 2.dp),
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
                Box(
                    modifier =
                        Modifier
                            .size(TimeCapsuleCalendarDesignToken.dateWidth.dp)
                            .background(
                                if (isFilled) MooiTheme.colorScheme.primary else MooiTheme.colorScheme.background,
                                shape = CircleShape,
                            ).border(
                                width = 1.5.dp,
                                color =
                                    if (isFilled) {
                                        Color.Transparent
                                    } else {
                                        Color(0xFFAECBFA).copy(
                                            alpha = 0.2f,
                                        )
                                    },
                                shape = CircleShape,
                            ),
                )
            }
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
                    .background(MooiTheme.colorScheme.background)
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
