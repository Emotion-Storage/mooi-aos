package com.emotionstorage.my.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.DayOfWeek

// TODO : 색상 확인 필요
@Composable
fun DayOfWeekSelector(
    modifier: Modifier = Modifier,
    selected: Set<DayOfWeek>,
    onToggle: (DayOfWeek) -> Unit,
    enabled: Boolean,
) {
    val days =
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
            horizontalArrangement = Arrangement.SpaceBetween,
            maxItemsInEachRow = 7,
        ) {
            days.forEach { dayOfWeek ->
                val isSelected = dayOfWeek in selected
                FilterChip(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(43.dp),
                    enabled = enabled,
                    selected = isSelected,
                    onClick = { onToggle(dayOfWeek) },
                    label = {
                        Text(
                            text = getDayOfWeekString(dayOfWeek),
                            style = MooiTheme.typography.body8,
                            textAlign = TextAlign.Center,
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    elevation =
                        FilterChipDefaults.filterChipElevation(),
                    border =
                        FilterChipDefaults.filterChipBorder(
                            enabled = enabled,
                            selected = isSelected,
                            borderColor = MooiTheme.colorScheme.background,
                            selectedBorderColor = MooiTheme.colorScheme.gray800,
                        ),
                    colors =
                        FilterChipDefaults.filterChipColors(
                            containerColor = Color.Black,
                            labelColor = Color.White,
                            selectedContainerColor = MooiTheme.colorScheme.background,
                            selectedLabelColor = MooiTheme.colorScheme.primary,
                        ),
                )
            }
        }
    }
}

private fun getDayOfWeekString(dayOfWeek: DayOfWeek): String =
    when (dayOfWeek) {
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
        DayOfWeek.SUNDAY -> "일"
    }

@Preview
@Composable
fun DayOfWeekSelectorPreview() {
    MooiTheme {
        DayOfWeekSelector(
            selected = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            onToggle = { dayofWeek ->
            },
            enabled = true,
        )
    }
}
