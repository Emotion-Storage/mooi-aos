package com.emotionstorage.my.ui.component

import android.widget.Space
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ToggleRow(
    modifier: Modifier = Modifier,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .padding(horizontal = 16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MooiTheme.typography.body7,
            color = Color.White,
        )
        Spacer(Modifier.weight(1f))

        ToggleSwitch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
        )
    }
}

@Composable
fun ToggleSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    val trackWidth = 57.dp
    val trackHeight = 30.dp
    val thumbSize = 26.dp
    val padding = ((trackHeight - thumbSize) / 2)

    val offset by animateDpAsState(
        if (checked) trackWidth - thumbSize - padding else padding,
        label = "thumbOffset"
    )

    val trackColor = if (checked) MooiTheme.colorScheme.secondary else MooiTheme.colorScheme.gray500
    val thumbColor = Color.White

    Box(
        modifier
            .size(trackWidth, trackHeight)
            .clip(RoundedCornerShape(trackHeight / 2))
            .background(trackColor)
            .clickable(enabled = enabled, onClick = { onCheckedChange(!checked) })
    ) {
        Box(
            Modifier
                .align(Alignment.CenterStart)
                .offset(x = offset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .align(Alignment.CenterStart)
        )
    }
}

@Preview
@Composable
fun ToggleRowPreview() {
    MooiTheme {

        var isChecked = true

        Column {
            ToggleRow(
                title = "MOOI 앱 푸시 알림",
                isChecked = isChecked,
                onCheckedChange = { value -> isChecked = false },
                enabled = true,
            )

            Spacer(modifier = Modifier.size(24.dp))

            ToggleRow(
                title = "MOOI 앱 푸시 알림",
                isChecked = !isChecked,
                onCheckedChange = { value -> isChecked = false },
                enabled = true,
            )

            Spacer(modifier = Modifier.size(24.dp))

            ToggleSwitch(
                checked = isChecked,
                onCheckedChange = { value -> isChecked = false },
                enabled = true,
            )

            Spacer(modifier = Modifier.size(24.dp))

            ToggleSwitch(
                checked = !isChecked,
                onCheckedChange = { value -> isChecked = false },
                enabled = true,
            )

        }
    }
}
