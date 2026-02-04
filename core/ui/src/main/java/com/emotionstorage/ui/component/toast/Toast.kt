package com.emotionstorage.ui.component.toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    iconId: Int? = null,
    outerPaddingValues: PaddingValues = PaddingValues(0.dp),
    innerPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 13.dp),
) {
    Box(modifier = modifier.padding(outerPaddingValues)) {
        Row(
            modifier =
                Modifier
                    .background(
                        Color(0xFF0E0C12).copy(alpha = 0.8f),
                        RoundedCornerShape(100),
                    ).padding(innerPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (iconId != null) {
                Image(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(iconId),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(9.dp))
            }

            Text(
                text = message,
                style = MooiTheme.typography.body7,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun ToastPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Toast(
                message =
                    "아직 보관을 확정하지 않은 감정이에요.\n오늘을 기준으로 타임캡슐\n회고 날짜를 지정해주세요.",
                outerPaddingValues = PaddingValues(top = 85.dp),
                innerPadding = PaddingValues(horizontal = 25.dp, vertical = 13.dp),
            )
        }
    }
}
