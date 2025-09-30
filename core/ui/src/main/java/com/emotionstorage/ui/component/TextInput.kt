package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

sealed class TextInputState(
    val message: String? = null,
) {
    class Empty(
        infoMessage: String? = null,
    ) : TextInputState(message = infoMessage)

    class Error(
        errorMessage: String? = null,
    ) : TextInputState(message = errorMessage)

    class Success(
        successMessage: String? = null,
    ) : TextInputState(message = successMessage)
}

@Composable
fun TextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    showCharCount: Boolean = true,
    maxCharCount: Int = 0,
    placeHolder: String = "",
    state: TextInputState = TextInputState.Empty(),
) {
    Column(modifier = modifier.background(MooiTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (!label.isNullOrEmpty()) {
                Text(
                    modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .padding(bottom = 18.dp),
                    style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                    color = Color.White,
                    text = label,
                )
            }
            if (showCharCount) {
                Row(
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(bottom = 18.dp),
                ) {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = MooiTheme.colorScheme.primary,
                        text = if (value.length > maxCharCount) maxCharCount.toString() else value.length.toString(),
                    )
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = Color.White,
                        text = "/$maxCharCount",
                    )
                }
            }
        }

        BasicTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 0.dp),
            textStyle = MooiTheme.typography.body3.copy(fontSize = 15.sp, color = Color.White),
            value = if (value.length > maxCharCount) value.substring(0, maxCharCount) else value,
            onValueChange = onValueChange,
            maxLines = 1,
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(9.dp),
            ) {
                if (value.isEmpty()) {
                    Text(
                        style = MooiTheme.typography.body3.copy(fontSize = 15.sp),
                        color = MooiTheme.colorScheme.gray500,
                        text = placeHolder,
                    )
                } else {
                    it()
                }

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                when (state) {
                                    is TextInputState.Empty -> MooiTheme.colorScheme.gray600
                                    is TextInputState.Error -> MooiTheme.colorScheme.errorRed
                                    is TextInputState.Success -> MooiTheme.colorScheme.primary
                                },
                            ),
                )
            }
        }

        TextInputMessage(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            state = state,
        )
    }
}

@Composable
private fun TextInputMessage(
    state: TextInputState,
    modifier: Modifier = Modifier,
) {
    if (state.message.isNullOrEmpty()) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter =
                painterResource(
                    when (state) {
                        is TextInputState.Empty -> R.drawable.caution
                        is TextInputState.Error -> R.drawable.caution
                        is TextInputState.Success -> R.drawable.success
                    },
                ),
            modifier = Modifier.size(18.dp),
            colorFilter =
                when (state) {
                    is TextInputState.Empty -> null
                    is TextInputState.Error -> ColorFilter.tint(MooiTheme.colorScheme.errorRed)
                    is TextInputState.Success -> null
                },
            contentDescription = null,
        )
        Text(
            style = MooiTheme.typography.body3,
            color =
                if (state is TextInputState.Error) {
                    MooiTheme.colorScheme.errorRed
                } else {
                    MooiTheme.colorScheme.primary
                },
            text = state.message,
        )
    }
}

@Preview
@Composable
private fun TextInputPreview() {
    val (value, setValue) = remember { mutableStateOf("") }

    MooiTheme {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextInput(
                label = "이름",
                value = value,
                onValueChange = setValue,
                placeHolder = "최소 2글자 이상의 이름을 적어주세요",
                showCharCount = true,
                maxCharCount = 8,
                state = TextInputState.Empty("2~8자리의 한글 또는 영문을 사용해주세요"),
            )
            TextInput(
                label = "이름",
                value = "뿅",
                onValueChange = {},
                showCharCount = true,
                maxCharCount = 8,
                state = TextInputState.Error("이름은 최소 2글자 이상이어야 합니다"),
            )
            TextInput(
                label = "이름",
                value = "찡찡이",
                onValueChange = {},
                showCharCount = true,
                maxCharCount = 8,
                state = TextInputState.Success("사용 가능한 이름입니다."),
            )
        }
    }
}
