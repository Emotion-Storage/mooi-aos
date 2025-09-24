package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TextBoxInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showCharCount: Boolean = true,
    maxCharCount: Int = 0,
    placeHolder: String = ""
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(MooiTheme.colorScheme.background, RoundedCornerShape(15.dp))
            .background(Color(0xFF0E0C12).copy(alpha = 0.5f), RoundedCornerShape(15.dp))
            .padding(18.dp),
        textStyle = MooiTheme.typography.body4.copy(
            fontWeight = FontWeight.Light,
            color = Color.White
        ),
        value = value,
        onValueChange = onValueChange
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Column(
                modifier = Modifier
                    .heightIn(min = 156.dp)
                    .fillMaxWidth()
            ) {
                if (value.isEmpty()) {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = MooiTheme.colorScheme.gray500,
                        text = placeHolder
                    )
                } else {
                    it()
                }
            }
            if (showCharCount) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 14.sp),
                        color = if (value.length == 0) MooiTheme.colorScheme.gray300 else MooiTheme.colorScheme.primary,
                        text = "${value.length}"
                    )
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 14.sp),
                        color = MooiTheme.colorScheme.gray300,
                        text = "/$maxCharCount"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TextBoxInputPreview() {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextBoxInput(
            value = "",
            onValueChange = {},
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000
        )
        TextBoxInput(
            value = "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. 사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
            onValueChange = {},
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000
        )
    }
}
