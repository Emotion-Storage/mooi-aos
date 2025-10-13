package com.emotionstorage.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    initialTime: LocalTime = LocalTime.now().withSecond(0).withNano(0),
    onTimeSelected: (LocalTime) -> Unit = {},
) {
    var spinnerTime by remember(initialTime) { mutableStateOf(initialTime) }

    BottomSheet(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(150.dp),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        confirmLabel = "확인",
        onConfirm = { onTimeSelected(spinnerTime) },
        hideDragHandle = false,
    ) {
        TimeWheelSpinner(
            selected = spinnerTime,
            onSelect = { spinnerTime = it },
            modifier = Modifier.padding(top = 8.dp, bottom = 40.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TimePickerBottomSheetPreview() {
    MooiTheme {

        TimePickerBottomSheet(
            onTimeSelected = { selectedTime ->

            },
        )
    }
}

