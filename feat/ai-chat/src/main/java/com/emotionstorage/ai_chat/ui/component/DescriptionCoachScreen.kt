package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun DescriptionCoachScreen(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(com.emotionstorage.ui.R.drawable.touch),
            contentDescription = "터치하세요",
            tint = Color.White,
        )

        Spacer(modifier = Modifier.padding(top = 8.dp))

        // fix : 폰트 조정되면 core.type에 넣기
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = "아무 곳이나 탭하세요.",
            color = MooiTheme.colorScheme.gray500,
            style = MooiTheme.typography.body2
        )

        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onCheckChanged(!checked)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckChanged,
                modifier = Modifier.padding(end = 0.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MooiTheme.colorScheme.primary,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.Black
                )
            )

            // fix : 폰트 조정되면 core.type에 넣기
            Text(
                text = "다시 보지 않기",
                color = MooiTheme.colorScheme.gray700,
                style = MooiTheme.typography.body4
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
            onCheckChanged = {}
        )
    }
}