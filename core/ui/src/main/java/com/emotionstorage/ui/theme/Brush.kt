package com.emotionstorage.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Immutable
data class MooiBrushScheme(
    val mainButtonBackground: Brush = Brush.linearGradient(
        listOf(
            Color(0x80849BEA),
            Color(0x14849BEA)
        ),
    ),
    val subButtonBackground: Brush = Brush.verticalGradient(listOf(Color.Black, Color.Yellow)),
    val subButtonBorder: Brush = Brush.verticalGradient(listOf(Color.Black, Color.Yellow)),
    val commentBackground: Brush = Brush.verticalGradient(
        listOf(
            Color(0x80849BEA),
            Color(0x14849BEA)
        )
    ),
)


@Preview(showBackground = true)
@Composable
private fun BrushPreview() {
    MooiTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(50.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MooiTheme.brushScheme.mainButtonBackground)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MooiTheme.brushScheme.subButtonBackground)
                    .border(
                        width = 1.dp,
                        brush = MooiTheme.brushScheme.subButtonBorder,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MooiTheme.brushScheme.commentBackground)
            )
        }
    }
}