package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Toast(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                Color(0xFF0E0C12).copy(alpha = 0.8f),
                RoundedCornerShape(100)
            )
            .padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MooiTheme.typography.body3,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SuccessToast(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                Color(0xFF0E0C12).copy(alpha = 0.8f),
                RoundedCornerShape(100)
            )
            .padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.padding(end = 9.dp),
            painter = painterResource(R.drawable.success_filled),
            contentDescription = null
        )

        Text(
            text = message,
            style = MooiTheme.typography.body3,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun ToastPreview() {
    MooiTheme {
        Column(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            SuccessToast("즐겨찾기가 설정되었습니다.")
            Toast("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요.")
        }
    }
}
