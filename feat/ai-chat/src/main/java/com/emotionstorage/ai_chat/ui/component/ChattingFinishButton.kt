package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.mainBackground

@Composable
fun ChattingFinishButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
    ) {
        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .mainBackground(true, RoundedCornerShape(500.dp))
                    .clickable {
                        onClick()
                    }.height(44.dp)
                    .padding(horizontal = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "대화 종료하기",
                style = MooiTheme.typography.body6.copy(color = Color.White),
            )
            Image(
                modifier =
                    Modifier
                        .size(8.dp, 14.dp)
                        .rotate(180f),
                painter = painterResource(R.drawable.arrow_back),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun ChattingFinishButtonPreview() {
    MooiTheme {
        ChattingFinishButton()
    }
}
