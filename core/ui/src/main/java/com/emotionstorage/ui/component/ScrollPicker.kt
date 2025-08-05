package com.emotionstorage.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

@Composable
fun ScrollPicker(
    selectedValue: String?,
    range: List<String>,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    val (isScrollPickerOpen, setScrollPickerOpen) = remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    // todo: add gradient bg & border when selected value exists
                    if (selectedValue != null) Color.Gray else Color.Black
                )
                .clickable(enabled = enabled) {
                    // toggle scroll picker open/close
                    setScrollPickerOpen(!isScrollPickerOpen)
                }
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                style = MooiTheme.typography.body3.copy(fontSize = 14.sp),
                color = if (selectedValue != null) MooiTheme.colorScheme.primary else MooiTheme.colorScheme.gray600,
                text = selectedValue ?: placeholder ?: "",
            )
            Image(
                painter = if (isScrollPickerOpen) painterResource(id = R.drawable.toggle_up) else painterResource(
                    id = R.drawable.toggle_down
                ),
                contentDescription = null,
            )
        }

        AnimatedVisibility(
            visible = isScrollPickerOpen,
            enter = slideInVertically {
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                expandFrom = Alignment.Top
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            // todo: add vertical scroll bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 144.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Black)
                    .padding(14.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                range.forEach {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 14.sp),
                        color = Color.White,
                        text = it,
                        modifier = Modifier
                            .height(24.dp)
                            .clickable(
                                onClick = {
                                    onValueChange(it)
                                    setScrollPickerOpen(false)
                                }
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScrollPickerPreview() {
    val (pickerValue, setPickerValue) = remember { mutableStateOf<String?>(null) }

    MooiTheme {
        ScrollPicker(
            selectedValue = pickerValue,
            placeholder = "월",
            onValueChange = setPickerValue,
            range = (1..12).toList().map { it.toString() },
            modifier = Modifier.width(78.dp)
        )
    }
}
