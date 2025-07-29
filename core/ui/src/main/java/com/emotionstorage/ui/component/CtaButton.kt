package com.emotionstorage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.EmotionStorageTheme
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun CtaButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        modifier = modifier
            .width(330.dp)
            .height(65.dp),
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MooiTheme.colorScheme.primary,
            contentColor = Color.White,
            disabledBackgroundColor = MooiTheme.colorScheme.gray500,
            disabledContentColor = Color.White
        ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(
            style = MooiTheme.typography.button,
            text = label
        )
    }
}

@Preview
@Composable
private fun CtaButtonPreview() {
    EmotionStorageTheme {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            CtaButton(
                label = "확인",
            )
            CtaButton(
                label = "확인",
                enabled = false
            )
        }
    }
}