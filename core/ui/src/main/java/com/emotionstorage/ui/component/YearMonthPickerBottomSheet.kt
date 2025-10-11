package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger
import java.time.YearMonth

private val CALENDER_MIN_YEAR_MONTH = YearMonth.of(1970, 1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthPickerBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    selectedYearMonth: YearMonth = YearMonth.now(),
    onYearMonthSelect: (YearMonth) -> Unit = {},
    minYearMonth: YearMonth = CALENDER_MIN_YEAR_MONTH,
    maxYearMonth: YearMonth = YearMonth.now(),
) {
    val (spinnerYearMonth, setSpinnerYearMonth) =
        remember(
            key1 = selectedYearMonth,
        ) { mutableStateOf(selectedYearMonth) }

    BottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        confirmLabel = "확인",
        onConfirm = {
            Logger.d("set calendar year month to $spinnerYearMonth")
            onYearMonthSelect(spinnerYearMonth)
        },
        hideDragHandle = true,
    ) {
        YearMonthWheelSpinner(
            yearMonthRange = minYearMonth to maxYearMonth,
            modifier = Modifier.padding(bottom = 48.dp),
            selectedYearMonth = spinnerYearMonth,
            onYearMonthSelect = {
                Logger.d("set spinner year month to $it")
                setSpinnerYearMonth(it)
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun YearMonthPickerBottomSheetPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) {
            YearMonthPickerBottomSheet(
                // open sheet state for preview
                sheetState =
                    rememberStandardBottomSheetState(
                        initialValue = SheetValue.Expanded,
                    ),
            )
        }
    }
}
