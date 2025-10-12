package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    val scope = rememberCoroutineScope()

    BottomSheet(
        hideDragHandle = true,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
    ) {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .height(356.dp),
        ) {
            // year & month selection
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp),
            ) {
                if (YearMonth.from(minDate) < calendarYearMonth) {
                    Image(
                        modifier =
                            Modifier
                                .align(Alignment.CenterStart)
                                .size(width = 8.dp, height = 14.dp)
                                .clickable {
                                    onYearMonthSelect(calendarYearMonth.minusMonths(1))
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
                                enabled = (calendarYearMonth < YearMonth.from(maxDate)),
                                onClick = onYearMonthDropdownClick,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "${calendarYearMonth.year}년 ${calendarYearMonth.monthValue}월",
                        style = MooiTheme.typography.body8,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )

                    Image(
                        modifier = Modifier.size(10.dp, 9.dp),
                        painter = painterResource(id = R.drawable.toggle_down),
                        contentDescription = "calendar year month picker",
                    )
                }

                if (calendarYearMonth < YearMonth.from(maxDate)) {
                    Image(
                        modifier =
                            Modifier
                                .align(Alignment.CenterEnd)
                                .size(width = 8.dp, height = 14.dp)
                                .rotate(180f)
                                .clickable {
                                    onYearMonthSelect(calendarYearMonth.plusMonths(1))
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
                                .padding(bottom = 11.dp),
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
                    Box(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(vertical = 8.dp),
                    ) {
                        if (date.year == calendarYearMonth.year && date.month == calendarYearMonth.month) {
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
                                            enabled = date.isAfter(minDate) && date.isBefore(maxDate),
                                            onClick = {
                                                scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                    if (!sheetState.isVisible) {
                                                        onDateSelect(date)
                                                    }
                                                }
                                            },
                                        ),
                            )
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = date.dayOfMonth.toString(),
                                style = MooiTheme.typography.body8,
                                color =
                                    Color.White.copy(
                                        alpha = if (date.isAfter(minDate) && date.isBefore(maxDate)) 1f else 0.13f,
                                    ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
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
                selectedDate = LocalDate.now(),
                minDate = LocalDate.now().minusDays(7),
                maxDate = LocalDate.now().plusDays(10),
            )
        }
    }
}
