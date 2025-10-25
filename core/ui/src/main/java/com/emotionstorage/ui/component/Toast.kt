package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    iconId: Int? = null,
    paddingValues: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 13.dp),
) {
    Row(
        modifier =
            modifier
                .background(
                    Color(0xFF0E0C12).copy(alpha = 0.8f),
                    RoundedCornerShape(100),
                ).padding(paddingValues),
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

@Composable
fun FavoriteToast(message: String) {
    Toast(
        message = message,
        iconId =
            if (message !=
                LocalContext.current.getString(R.string.toast_favorite_full)
            ) {
                R.drawable.success_filled
            } else {
                null
            },
    )
}

@Preview
@Composable
private fun ToastPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Toast(
                "아직 보관을 확정하지 않은 감정이에요.\n오늘을 기준으로 타임캡슐\n회고 날짜를 지정해주세요.",
                paddingValues = PaddingValues(horizontal = 25.dp, vertical = 13.dp),
            )
            FavoriteToast(
                LocalContext.current.getString(R.string.toast_favorite_added),
            )
            FavoriteToast(
                LocalContext.current.getString(R.string.toast_favorite_full),
            )
        }
    }
}
