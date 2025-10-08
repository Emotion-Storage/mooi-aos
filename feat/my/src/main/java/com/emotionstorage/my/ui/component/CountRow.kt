package com.emotionstorage.my.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun CountRow(
    count: Int,
    modifier: Modifier = Modifier,
) {
    val digits = remember(count) { count.toString().map { it } }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            digits.forEach { char -> DigitBox(char) }
        }

        Spacer(Modifier.width(8.dp))
        Text(
            text = "개",
            style = MooiTheme.typography.head2,
            color = Color.White,
        )
    }
}

@Composable
private fun DigitBox(char: Char) {
    Box(
        modifier =
            Modifier
                .widthIn(37.dp)
                .heightIn(57.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    color = MooiTheme.colorScheme.blueGrayBackground,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = char.toString(),
            style = MooiTheme.typography.head1,
            color = MooiTheme.colorScheme.secondary,
        )
    }
}

@Preview
@Composable
fun CountRowPreview() {
    MooiTheme {
        Column {
            CountRow(
                count = 123,
            )
            Spacer(Modifier.size(8.dp))
            CountRow(
                count = 23,
            )
            Spacer(Modifier.size(8.dp))
            CountRow(
                count = 1,
            )
        }
    }
}
