package com.emotionstorage.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard

@Composable
fun IconWithCount(
    modifier: Modifier = Modifier,
    @DrawableRes
    iconId: Int,
    count: Int? = null,
    iconSizeDp: Int = 30,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .clickable(
                    enabled = onClick != null,
                    onClick = {
                        onClick?.invoke()
                    },
                ),
    ) {
        Image(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(iconSizeDp.dp),
            painter = painterResource(id = iconId),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )

        if (count != null) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .sizeIn(minWidth = 9.dp, minHeight = 12.dp),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = if (count > 99) "99+" else count.toString(),
                    style =
                        TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            lineHeight = 20.sp,
                            color = Color.White,
                        ),
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconWithCountPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.background(MooiTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconWithCount(
                modifier = Modifier.size(32.dp),
                iconId = R.drawable.ic_key,
                count = null,
            )

            IconWithCount(
                modifier = Modifier.size(32.dp),
                iconId = R.drawable.ic_key,
                count = 3,
            )

            IconWithCount(
                modifier = Modifier.size(32.dp),
                iconId = R.drawable.ic_key,
                count = 100,
            )
        }
    }
}
