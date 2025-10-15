package com.emotionstorage.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun BulletListItem(
    text: String,
    bulletSize: Dp = 4.dp,
    gap: Dp = 8.dp,
    blockIndentStart: Dp = 16.dp,
    color: Color = Color.White,
    bulletColor: Color = Color(0xFFDADADA),
) {
    val textStyle = MooiTheme.typography.caption3.copy(lineHeight = 22.sp)

    val firstLineHeight = with(LocalDensity.current) { textStyle.lineHeight.toDp() }

    HangingListItem(
        prefix = { Canvas(Modifier.size(bulletSize)) { drawCircle(color = bulletColor) } },
        text = text,
        textStyle = textStyle,
        textColor = color,
        blockIndentStart = blockIndentStart,
        gap = gap,
        minPrefixWidth = bulletSize,
        firstLineHeight = firstLineHeight / 2,
    )
}

@Composable
@Preview
fun BulletListItemPreview() {
    MooiTheme {
        BulletListItem(
            text = "테스트입니다.",
        )
    }
}
