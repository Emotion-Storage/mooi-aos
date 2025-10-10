package com.emotionstorage.my.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun NumberedListItem(
    index: Int,
    text: String,
    gap: Dp = 8.dp,
    blockIndentStart: Dp = 8.dp,
    color: Color = Color.White,
) {
    val label = "$index."
    val textStyle= MooiTheme.typography.caption3.copy(lineHeight = 22.sp)

    HangingListItem(
        prefix = { Text(label, style = textStyle, color = color) },
        prefixTextForMeasure = label,
        text = text,
        blockIndentStart = blockIndentStart,
        textStyle = textStyle,
        textColor = color,
        gap = gap,
    )
}

@Composable
@Preview
fun NumberedListItemPreview() {
    MooiTheme {
        NumberedListItem(index = 1, text = "테스트")
    }
}
