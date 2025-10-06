package com.emotionstorage.my.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
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
    style: TextStyle =
        TextStyle(
            fontSize = 13.sp,
            lineHeight = 22.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
    color: Color = Color.White,
) {
    val label = "$index."

    HangingListItem(
        prefix = { Text(label, style = style, color = color) },
        prefixTextForMeasure = label,
        text = text,
        blockIndentStart = blockIndentStart,
        textStyle = style,
        textColor = color,
        gap = gap,
    )
}

@Composable
@Preview
fun NuberedListItemPreview() {
    MooiTheme {
        NumberedListItem(index = 1, text = "테스트")
    }
}
