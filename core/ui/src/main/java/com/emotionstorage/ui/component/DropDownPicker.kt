package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

@Composable
fun DropDownPicker(
    selectedValue: String,
    modifier: Modifier = Modifier,
    options: List<String> = emptyList(),
    onSelect: (String) -> Unit = {},
    enabled: Boolean = true,
) {
    val (isScrollPickerOpen, setScrollPickerOpen) = remember { mutableStateOf(false) }

    Column(
        modifier = modifier.background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Black, RoundedCornerShape(10.dp))
                .padding(horizontal = 12.dp)
                .clickable(enabled = enabled) {
                    // toggle scroll picker open/close
                    setScrollPickerOpen(!isScrollPickerOpen)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                style = MooiTheme.typography.body3.copy(fontSize = 14.sp),
                color = MooiTheme.colorScheme.gray300,
                text = selectedValue,
            )
            Image(
                modifier = Modifier
                    .width(10.dp)
                    .height(9.dp),
                painter = if (isScrollPickerOpen) painterResource(id = R.drawable.toggle_up) else painterResource(
                    id = R.drawable.toggle_down
                ),
                colorFilter = ColorFilter.tint(MooiTheme.colorScheme.gray300),
                contentDescription = null,
            )
        }

        // todo: set drop down menu width same as parent
        DropdownMenu(
            expanded = isScrollPickerOpen,
            onDismissRequest = { setScrollPickerOpen(false) },
            shape = RoundedCornerShape(10.dp),
            containerColor = Color.Black
        ) {
            options.forEachIndexed { index, it ->
                DropdownMenuItem(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    text = {
                        Text(
                            style = MooiTheme.typography.body3.copy(
                                fontSize = 14.sp,
                                lineHeight = 24.sp
                            ),
                            color = Color.White,
                            text = it,
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        onSelect(it)
                                        setScrollPickerOpen(false)
                                    }
                                )
                        )
                    },
                    onClick = {
                        onSelect(it)
                        setScrollPickerOpen(false)
                    }
                )
                if (index != options.lastIndex) {
                    Spacer(
                        modifier = Modifier
                            .height(1.5.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .background(
                                MooiTheme.colorScheme.gray900,
                                RoundedCornerShape(10.dp)
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DropDownPickerPreview() {
    val options = listOf("최신 날짜순", "즐겨찾기순")
    val (pickerValue, setPickerValue) = remember { mutableStateOf<String>(options[0]) }

    MooiTheme {
        Column(modifier = Modifier.background(MooiTheme.colorScheme.background)) {
            DropDownPicker(
                modifier = Modifier.width(102.dp),
                selectedValue = pickerValue,
                onSelect = setPickerValue,
                options = options
            )

            // picker should open above this box
            Box(
                modifier = Modifier
                    .size(102.dp, 100.dp)
                    .background(Color.Gray)
            )
        }
    }
}
