package com.emotionstorage.ui.component.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.getWeekDatesOfTargetMonth
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun CalendarDates(
    calendarYearMonth: YearMonth,
    modifier: Modifier = Modifier,
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
    // calendar dates
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.Fixed(7),
    ) {
        items(
            items = calendarYearMonth.getWeekDatesOfTargetMonth(),
            key = { it.toString() },
        ) { date ->
            dateItem(Modifier, date)
        }
    }
}

@Preview
@Composable
private fun CalendarDatesPreview() {
    MooiTheme {
        CalendarDates(
            modifier = Modifier.background(MooiTheme.colorScheme.background),
            calendarYearMonth = YearMonth.now(),
        )
    }
}
