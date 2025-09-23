package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

private val TOAST_ZINDEX = 10f

data class ToastState(
    val message: String = "", val showCheckIcon: Boolean = false
)

@Composable
fun BoxScope.Toast(
    modifier: Modifier = Modifier,
    showToast: Boolean = false,
    state: ToastState = ToastState()
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp

    if (showToast) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .offset(y = (screenHeight * 0.8).dp)
                .zIndex(TOAST_ZINDEX)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                        Color(0xFF0E0C12).copy(alpha = 0.8f), RoundedCornerShape(100)
                    )
                    .padding(horizontal = 20.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (state.showCheckIcon) {
                    Image(
                        modifier = Modifier.padding(end = 9.dp),
                        painter = painterResource(R.drawable.success_filled),
                        contentDescription = null,
                    )
                }
                Text(
                    text = state.message,
                    style = MooiTheme.typography.body3,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun CustomToastPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.fillMaxSize().background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box (modifier = Modifier.fillMaxSize()){
                Toast(
                    showToast = true,
                    state = ToastState(message = "즐겨찾기가 해제되었습니다.", showCheckIcon = true)
                )
//                Toast(
//                    showToast = true,
//                    state = ToastState(message = "내 마음 서랍이 꽉 찼어요. \uD83D\uDE22\n즐겨찾기 중 일부를 해제해주세요.")
//                )
            }
        }
    }
}