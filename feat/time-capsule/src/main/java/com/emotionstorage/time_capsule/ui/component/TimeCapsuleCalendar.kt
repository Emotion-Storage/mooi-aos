package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.getWeekDatesOfTargetMonth
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.YearMonth

private val CALENDAR_MIN_YEAR_MONTH = YearMonth.of(1970, 1)

private object TimeCapsuleCalendarDesignToken {
    val calendarWidthDp = 330
    val dateWidthDp = 30
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
    showYearMonthDropDownIcon: Boolean = false,
    onYearMonthDropDownIconClick: () -> Unit = {},
    timeCapsuleDates: List<LocalDate> = emptyList(),
    onDateSelect: (LocalDate) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .width(TimeCapsuleCalendarDesignToken.calendarWidthDp.dp)
                .background(MooiTheme.colorScheme.background),
    ) {
        // year & month selection
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 17.dp)
                    .padding(horizontal = 1.dp),
        ) {
            if (YearMonth.from(CALENDAR_MIN_YEAR_MONTH) < calendarYearMonth) {
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
                    style = MooiTheme.typography.mainButton,
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

            if (calendarYearMonth < YearMonth.now()) {
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

        // calendar dates
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(
                items = listOf("일", "월", "화", "수", "목", "금", "토"),
                key = { it },
            ) { label ->
                Text(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp),
                    text = label,
                    style = MooiTheme.typography.caption5,
                    color = MooiTheme.colorScheme.gray400,
                    textAlign = TextAlign.Center,
                )
            }

            items(
                items = calendarYearMonth.getWeekDatesOfTargetMonth(),
                key = { it.toString() },
            ) { date ->
                DateItem(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(bottom = 12.dp),
                    date = date,
                    onClick = onDateSelect,
                    isShown = date.year == calendarYearMonth.year && date.month == calendarYearMonth.month,
                    isFilled = date in timeCapsuleDates,
                    isToday = date == LocalDate.now(),
                )
            }
        }
    }
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
                        .padding(top = 6.dp, bottom = 4.dp),
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
                            .size(TimeCapsuleCalendarDesignToken.dateWidthDp.dp)
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
fun TimeCapsuleCalendarPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) {
            TimeCapsuleCalendar(
                modifier = Modifier.align(Alignment.Center),
                calendarYearMonth = calendarYearMonth,
                onCalendarYearMonthSelect = setCalendarYearMonth,
                timeCapsuleDates = DUMMY_TIME_CAPSULE_DATES,
                showYearMonthDropDownIcon = true,
            )
        }
    }
}
