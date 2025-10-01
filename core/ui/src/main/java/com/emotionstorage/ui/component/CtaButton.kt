package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.mainBackground

private object CtaButtonDesignToken {
    val height = 65.dp
    val width = 330.dp
}

enum class CtaButtonType {
    FILLED,
    TONAL,
}

@Composable
fun CtaButton(
    modifier: Modifier = Modifier,
    labelString: String? = null,
    labelContent: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    type: CtaButtonType = CtaButtonType.FILLED,
    radius: Int = 15,
    isDefaultWidth: Boolean = true,
    isDefaultHeight: Boolean = true,
    textStyle: TextStyle = MooiTheme.typography.mainButton,
) {
    Box(modifier = modifier) {
        Button(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .run {
                        if (isDefaultWidth) {
                            this.width(CtaButtonDesignToken.width)
                        } else {
                            this.fillMaxWidth()
                        }
                    }.run {
                        if (isDefaultHeight) {
                            this.height(CtaButtonDesignToken.height)
                        } else {
                            this.fillMaxHeight()
                        }
                    }.mainBackground(enabled, RoundedCornerShape(radius.dp)),
            shape = RoundedCornerShape(radius.dp),
            elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        when (type) {
                            CtaButtonType.FILLED -> Color.Transparent
                            CtaButtonType.TONAL -> MooiTheme.colorScheme.gray700
                        },
                    contentColor =
                        when (type) {
                            CtaButtonType.FILLED -> Color.White
                            CtaButtonType.TONAL -> MooiTheme.colorScheme.gray500
                        },
                    disabledContainerColor = MooiTheme.colorScheme.gray500,
                    disabledContentColor = Color.White,
                ),
            enabled = enabled,
            onClick = onClick,
        ) {
            if(labelString != null) {
                Text(
                    style = textStyle,
                    text = labelString,
                )
            }
            if(labelContent != null){
                labelContent()
            }
        }
    }
}

@Preview
@Composable
private fun CtaButtonPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            CtaButton(
                labelString = "확인",
            )
            CtaButton(
                labelString = "확인",
                type = CtaButtonType.TONAL,
            )
            CtaButton(
                labelString = "확인",
                enabled = false,
            )
            CtaButton(
                labelContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(id = R.drawable.alarm),
                            contentDescription = "alarm",
                        )
                        Text(
                            text = "Custom Content Button",
                            style = MooiTheme.typography.mainButton,
                            color = Color.White,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                },
            )
        }
    }
}
