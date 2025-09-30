package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.YearMonth

private val MIN_YEAR_MONTH = YearMonth.of(1970, 1)

@Composable
fun YearMonthWheelSpinner(
    modifier: Modifier = Modifier,
    selectedYearMonth: YearMonth = YearMonth.now(),
    onYearMonthSelect: (YearMonth) -> Unit = {},
    minYearMonth: YearMonth = MIN_YEAR_MONTH,
    maxYearMonth: YearMonth = YearMonth.now(),
) {
    val yearRange = remember(key1 = minYearMonth, key2 = maxYearMonth) { (minYearMonth.year..maxYearMonth.year) }
    val monthRange =
        remember(key1 = selectedYearMonth, key2 = minYearMonth, key3 = maxYearMonth) {
            if (selectedYearMonth.year == minYearMonth.year) {
                (minYearMonth.monthValue..12)
            } else if (selectedYearMonth.year == maxYearMonth.year) {
                (1..maxYearMonth.monthValue)
            } else {
                (1..12)
            }
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .height(38.dp)
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.dropBox, RoundedCornerShape(15.dp)),
        )
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
        ) {
            WheelSpinner(
                modifier = Modifier.width(58.dp),
                items = yearRange.map { "${it}년" },
                selectedItem = selectedYearMonth.year.toString() + "년",
                onItemSelect = { it ->
                    val selectedYearMonth = YearMonth.of(it.dropLast(1).toInt(), selectedYearMonth.monthValue)
                    onYearMonthSelect(
                        if (selectedYearMonth.isAfter(maxYearMonth)) {
                            maxYearMonth
                        } else if (selectedYearMonth.isBefore(minYearMonth)) {
                            minYearMonth
                        } else {
                            selectedYearMonth
                        },
                    )
                },
                showCenterIndicator = false,
            )
            Spacer(modifier = Modifier.width(66.dp))
            WheelSpinner(
                modifier = Modifier.width(35.dp),
                items = monthRange.map { "${it}월" },
                selectedItem = selectedYearMonth.monthValue.toString() + "월",
                onItemSelect = { it ->
                    onYearMonthSelect(YearMonth.of(selectedYearMonth.year, it.dropLast(1).toInt()))
                },
                showCenterIndicator = false,
            )
        }
    }
}

@Preview
@Composable
private fun YearMonthWheelSpinnerPreview() {
    var selected by remember { mutableStateOf<YearMonth>(YearMonth.now()) }

    MooiTheme {
        Box(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
        ) {
            YearMonthWheelSpinner(
                modifier = Modifier.align(Alignment.Center),
                selectedYearMonth = selected,
                onYearMonthSelect = { selected = it },
            )
        }
    }
}
