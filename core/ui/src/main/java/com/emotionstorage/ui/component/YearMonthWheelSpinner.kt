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

@Composable
fun YearMonthWheelSpinner(
    yearMonthRange: Pair<YearMonth, YearMonth>,
    modifier: Modifier = Modifier,
    selectedYearMonth: YearMonth = YearMonth.now(),
    onYearMonthSelect: (YearMonth) -> Unit = {},
) {
    require(yearMonthRange.first <= yearMonthRange.second) { "minYearMonth must be less than or equal to maxYearMonth" }

    // wheel spinner items
    val yearRange = remember(key1 = yearMonthRange) { (yearMonthRange.first.year..yearMonthRange.second.year) }
    val monthRange =
        remember(key1 = selectedYearMonth, key2 = yearMonthRange) {
            // if min year and max year are the same
            if (selectedYearMonth.year == yearMonthRange.first.year && selectedYearMonth.year == yearMonthRange.second.year) {
                yearMonthRange.first.monthValue..yearMonthRange.second.monthValue
            } else if (selectedYearMonth.year == yearMonthRange.first.year) {
                yearMonthRange.first.monthValue..12
            } else if (selectedYearMonth.year == yearMonthRange.second.year) {
                1..yearMonthRange.second.monthValue
            } else {
                1..12
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
                    var selectedYearMonth = YearMonth.of(it.dropLast(1).toInt(), selectedYearMonth.monthValue)
                    if(selectedYearMonth.isAfter(yearMonthRange.second)){
                        selectedYearMonth = yearMonthRange.second
                    }else if(selectedYearMonth.isBefore(yearMonthRange.first)){
                        selectedYearMonth = yearMonthRange.first
                    }

                    onYearMonthSelect(selectedYearMonth)
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
                yearMonthRange = YearMonth.of(2020, 3) to YearMonth.now(),
                modifier = Modifier.align(Alignment.Center),
                selectedYearMonth = selected,
                onYearMonthSelect = { selected = it },
            )
        }
    }
}
