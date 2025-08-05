package com.emotionstorage.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun CtaButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Box(modifier = modifier) {
        Button(
            modifier = Modifier
                .width(330.dp)
                .height(65.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(15.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MooiTheme.colorScheme.tertiary,
                contentColor = Color.White,
                disabledContainerColor = MooiTheme.colorScheme.gray500,
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
}

@Preview
@Composable
private fun CtaButtonPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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