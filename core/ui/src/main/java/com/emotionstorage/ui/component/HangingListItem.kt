package com.emotionstorage.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import kotlin.math.max

@Composable
fun HangingListItem(
    prefix: @Composable () -> Unit,
    prefixTextForMeasure: String? = null,
    text: String,
    textStyle: TextStyle,
    textColor: Color,
    gap: Dp = 6.dp,
    minPrefixWidth: Dp = 0.dp,
    blockIndentStart: Dp = 0.dp,
    firstLineHeight: Dp? = null,
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val measuredPx =
        prefixTextForMeasure?.let {
            measurer
                .measure(AnnotatedString(it), style = textStyle)
                .size
                .width
                .toFloat()
        } ?: 0f

    val leadingWidthDp =
        with(density) { max(measuredPx, minPrefixWidth.toPx()).toDp() } + gap

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = blockIndentStart),
    ) {
        Box(Modifier.width(leadingWidthDp)) {
            if (firstLineHeight != null) {
                Box(Modifier.height(firstLineHeight).fillMaxWidth()) {
                    Box(Modifier.align(Alignment.CenterStart)) { prefix() }
                }
            } else {
                prefix()
            }
        }
        Text(
            text = text,
            style = textStyle,
            color = textColor,
            modifier = Modifier.weight(1f, fill = false),
        )
    }
}

@Composable
@Preview
fun HangingListItemPreview() {
    MooiTheme {
        HangingListItem(
            prefix = { Text("테스트") },
            text = "테스트입니다.",
            textStyle = MooiTheme.typography.body1,
            textColor = Color.White,
        )
    }
}
