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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalTime

@Composable
fun TimeWheelSpinner(
    selected: LocalTime,
    onSelect: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isPm = selected.hour >= 12
    val hour12 = ((selected.hour + 11) % 12) + 1
    val minuteStep = 5
    val minuteItems = (0..55 step minuteStep).toList()

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 20.dp),
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
                    .align(Alignment.Center)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WheelSpinner(
                modifier = Modifier.width(68.dp),
                items = listOf("오전", "오후"),
                selectedItem = if (isPm) "오후" else "오전",
                onItemSelect = { ap ->
                    val newHour24 =
                        if (ap == "오전") {
                            if (hour12 == 12) 0 else hour12 % 12
                        } else {
                            if (hour12 == 12) 12 else (hour12 % 12) + 12
                        }
                    onSelect(selected.withHour(newHour24))
                },
                showCenterIndicator = false,
            )

            Spacer(Modifier.width(12.dp))

            WheelSpinner(
                modifier = Modifier.width(52.dp),
                items = (1..12).map(Int::toString),
                selectedItem = hour12.toString(),
                onItemSelect = { h12 ->
                    val h = h12.toInt()
                    val newHour24 =
                        if (isPm) {
                            if (h == 12) 12 else (h % 12) + 12
                        } else {
                            if (h == 12) 0 else h % 12
                        }
                    onSelect(selected.withHour(newHour24))
                },
                showCenterIndicator = false,
            )

            Text(
                text = " : ",
                style = MooiTheme.typography.body1,
                color = MooiTheme.colorScheme.gray400,
                modifier = Modifier.padding(horizontal = 4.dp),
            )

            val snappedMinute = (selected.minute / minuteStep) * minuteStep
            WheelSpinner(
                modifier = Modifier.width(52.dp),
                items = minuteItems.map { it.toString().padStart(2, '0') },
                selectedItem = snappedMinute.toString().padStart(2, '0'),
                onItemSelect = { mm -> onSelect(selected.withMinute(mm.toInt())) },
                showCenterIndicator = false,
            )
        }
    }
}

@Preview
@Composable
private fun TimeWheelSpinnerPreview() {
    MooiTheme {
        TimeWheelSpinner(
            selected = LocalTime.now(),
            onSelect = {},
            modifier = Modifier.padding(10.dp),
        )
    }
}
