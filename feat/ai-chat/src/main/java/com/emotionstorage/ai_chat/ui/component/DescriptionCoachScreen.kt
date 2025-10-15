package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

@Composable
fun DescriptionCoachScreen(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter =
                painterResource(
                    R
                        .drawable
                        .touch,
                ),
            contentDescription = "터치하세요",
            tint = MooiTheme.colorScheme.gray500,
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        Text(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp),
            text = "아무 곳이나 탭하세요.",
            color = MooiTheme.colorScheme.gray500,
            style = MooiTheme.typography.body5,
        )

        Spacer(modifier = Modifier.size(12.dp))

        Row(
            modifier =
                Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {
                        onCheckChanged(!checked)
                    },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (checked) {
                Icon(
                    painter = painterResource(R.drawable.checkbox_on),
                    contentDescription = "체크상태",
                    tint = MooiTheme.colorScheme.primary,
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.checkbox_off),
                    contentDescription = "체크상태",
                    tint = MooiTheme.colorScheme.gray700,
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "다시 보지 않기",
                color = MooiTheme.colorScheme.gray700,
                style = MooiTheme.typography.caption2.copy(lineHeight = 20.sp),
            )
        }
    }
}

@Preview
@Composable
fun DescriptionCoachScreenPreview() {
    MooiTheme {
        DescriptionCoachScreen(
            checked = false,
            onCheckChanged = {},
        )
    }
}
