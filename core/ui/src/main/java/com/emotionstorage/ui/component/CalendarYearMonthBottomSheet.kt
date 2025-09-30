package com.emotionstorage.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orhanobut.logger.Logger
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarYearMonthBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = {},
    selectedYearMonth: YearMonth = YearMonth.now(),
    onYearMonthSelect: (YearMonth) -> Unit = {},
) {
    val (spinnerYearMonth, setSpinnerYearMonth) = remember(key1 = selectedYearMonth) { mutableStateOf(selectedYearMonth) }

    BottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmLabel = "확인",
        onConfirm = {
            Logger.d("set calendar year month to $spinnerYearMonth")
            onYearMonthSelect(spinnerYearMonth)
        },
    ) {
        YearMonthWheelSpinner(
            modifier = Modifier.padding(bottom = 48.dp),
            selectedYearMonth = spinnerYearMonth,
            onYearMonthSelect = {
                Logger.d("set spinner year month to $it")
                setSpinnerYearMonth(it)
            },
        )
    }
}
