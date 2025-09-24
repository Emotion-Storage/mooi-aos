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

private val MIN_CALENDAR_DATE = LocalDate.of(2023, 1, 1)

private val DUMMY_TIME_CAPSULE_DATES =
    (1..31).toList().filter {
        it % 3 == 0
    }.map {
        LocalDate.of(LocalDate.now().year, LocalDate.now().month, it)
    }

@Composable
fun TimeCapsuleCalendar(
    modifier: Modifier = Modifier,
    calendarDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    onCalendarDateSelect: (calendarDate: LocalDate) -> Unit = {},
    timeCapsuleDates: List<LocalDate> = emptyList(),
    onDateSelect: (LocalDate) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background),
    ) {
        // year & month selection
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 17.dp),
        ) {
            if (MIN_CALENDAR_DATE < calendarDate) {
                Image(
                    modifier =
                        Modifier
                            .align(Alignment.CenterStart)
                            .size(14.dp)
                            .clickable {
                                onCalendarDateSelect(calendarDate.minusMonths(1))
                            },
                    painter = painterResource(id = R.drawable.arrow_back),
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                    contentDescription = "",
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "${calendarDate.year}년 ${calendarDate.monthValue}월",
                style = MooiTheme.typography.button,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
            if (calendarDate < LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1)) {
                Image(
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .size(14.dp)
                            .rotate(180f)
                            .clickable {
                                onCalendarDateSelect(calendarDate.plusMonths(1))
                            },
                    painter = painterResource(id = R.drawable.arrow_back),
                    colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
                    contentDescription = "",
                )
            }
        }

        // days of week
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
        ) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { label ->
                Text(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(24.dp),
                    text = label,
                    style = MooiTheme.typography.body5,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }
        }

        // calendar dates
        LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.fillMaxWidth()) {
            items(
                items = calendarDate.getWeekDatesOfTargetMonth(),
                key = { it.toString() },
            ) { date ->
                DateItem(
                    modifier = Modifier.padding(bottom = 12.dp),
                    date = date,
                    onClick = onDateSelect,
                    isShown = date.year == calendarDate.year && date.month == calendarDate.month,
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
    Box(modifier = modifier) {
        if (!isShown) {
            Spacer(
                Modifier
                    .width(37.dp)
                    .height(63.dp),
            )
        } else {
            Column(
                modifier =
                    Modifier
                        .width(37.dp)
                        .height(63.dp)
                        .background(
                            if (isToday) MooiTheme.colorScheme.secondary else Color.Transparent,
                            shape = RoundedCornerShape(20.dp),
                        )
                        .clickable { onClick(date) },
                verticalArrangement =
                    Arrangement.spacedBy(
                        9.dp,
                        alignment = Alignment.CenterVertically,
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = date.dayOfMonth.toString(),
                    style = MooiTheme.typography.body5,
                    color = Color.White,
                )
                Box(
                    modifier =
                        Modifier
                            .size(30.dp)
                            .background(
                                if (isFilled) MooiTheme.colorScheme.primary else MooiTheme.colorScheme.background,
                                shape = CircleShape,
                            )
                            .border(
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
    val (calendarDate, setCalendarDate) =
        remember {
            mutableStateOf(LocalDate.of(LocalDate.now().year, LocalDate.now().month, 1))
        }
    MooiTheme {
        TimeCapsuleCalendar(
            calendarDate = calendarDate,
            onCalendarDateSelect = setCalendarDate,
            timeCapsuleDates = DUMMY_TIME_CAPSULE_DATES,
        )
    }
}
