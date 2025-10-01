package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.component.TextBoxInput
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TimeCapsuleNote(
    modifier: Modifier = Modifier,
    note: String? = null,
    onNoteChange: (note: String) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(17.dp),
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = "나의 회고 일기",
                style = MooiTheme.typography.body1.copy(lineHeight = 24.sp),
                color = Color.White,
            )
            Text(
                text = "그때의 감정과 지금 달라진것이 있나요?\n감정을 회고하며 노트를 작성해보세요.",
                style = MooiTheme.typography.caption7.copy(lineHeight = 20.sp),
                textAlign = TextAlign.Center,
                color = MooiTheme.colorScheme.gray400,
            )
        }

        TextBoxInput(
            modifier = Modifier.fillMaxWidth(),
            value = note ?: "",
            onValueChange = onNoteChange,
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000,
        )
    }
}
