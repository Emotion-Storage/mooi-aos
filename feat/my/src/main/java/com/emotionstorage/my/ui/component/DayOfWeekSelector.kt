package com.emotionstorage.my.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.5.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            val isSelected = day in selected
            DayChip(
                text = getDayOfWeekString(day),
                selected = isSelected,
                enabled = enabled,
                onClick = { onToggle(day) }
            )
        }
    }

}

@Composable
private fun DayChip(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(10.dp)
    val borderColor =
        if (selected) MooiTheme.brushScheme.subButtonBorder else SolidColor(Color.Transparent)
    val containerColor =
        if (selected) MooiTheme.brushScheme.subButtonBackground else SolidColor(Color.Black)
    val labelColor =
        if (selected) MooiTheme.colorScheme.primary else Color.White

    Box(
        modifier = Modifier
            .size(width = 43.dp, height = 43.dp)
            .clip(shape)
            .border(1.dp, brush = borderColor, shape)
            .background(containerColor)
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MooiTheme.typography.body8,
            color = labelColor
        )
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
