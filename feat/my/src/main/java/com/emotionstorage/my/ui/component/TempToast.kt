package com.emotionstorage.my.ui.component

import android.support.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

// TODO : CORE Component Success 토스트와 통합 필요해보임
@Composable
fun TempToast(
    modifier: Modifier,
    message: String,
    @DrawableRes resId: Int,
) {
    Row(
        modifier =
            modifier
                .background(
                    Color(0xFF0E0C12).copy(alpha = 0.8f),
                    RoundedCornerShape(100),
                ).padding(horizontal = 20.dp, vertical = 13.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier = Modifier.padding(end = 9.dp),
            painter = painterResource(resId),
            contentDescription = null,
        )

        Text(
            text = message,
            style = MooiTheme.typography.body7.copy(lineHeight = 24.sp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun TempToastPreview() {
    MooiTheme {
        TempToast(
            modifier = Modifier,
            message = "이메일이 복사되었습니다.",
            resId = R.drawable.mail,
        )
    }
}
