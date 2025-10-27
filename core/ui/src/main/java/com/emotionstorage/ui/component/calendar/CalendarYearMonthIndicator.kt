package com.emotionstorage.ui.component.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import java.time.YearMonth

@Composable
fun CalendarYearMonthIndicator(
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
            ArrowIcon(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart),
                onClick = {
                    onCalendarYearMonthSelect(calendarYearMonth.minusMonths(1))
                },
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
            ArrowIcon(
                Modifier
                    .align(Alignment.CenterEnd)
                    .rotate(180f),
                onClick = {
                    onCalendarYearMonthSelect(calendarYearMonth.plusMonths(1))
                },
            )
        }
    }
}

@Composable
private fun ArrowIcon(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Image(
        modifier =
            modifier
                .size(width = 8.dp, height = 14.dp)
                .clickable {
                    onClick()
                },
        painter = painterResource(id = R.drawable.arrow_back),
        colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray600),
        contentDescription = "",
    )
}

@Preview
@Composable
private fun CalendarYearMonthIndicatorPreview() {
    val (calendarYearMonth, setCalendarYearMonth) = remember { mutableStateOf(YearMonth.now()) }

    MooiTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CalendarYearMonthIndicator(
                calendarYearMonth = calendarYearMonth,
                minYearMonth = YearMonth.now().minusYears(1),
                maxYearMonth = YearMonth.now().plusYears(1),
                onCalendarYearMonthSelect = setCalendarYearMonth,
            )
            CalendarYearMonthIndicator(
                calendarYearMonth = calendarYearMonth,
                minYearMonth = YearMonth.now().minusYears(1),
                maxYearMonth = YearMonth.now().plusYears(1),
                onCalendarYearMonthSelect = setCalendarYearMonth,
                showYearMonthDropDownIcon = true,
            )
        }
    }
}
