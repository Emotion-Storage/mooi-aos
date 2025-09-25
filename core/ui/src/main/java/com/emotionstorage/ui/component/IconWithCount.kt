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
    count: Int,
    iconSizeDp: Int = 22,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .padding(horizontal = 2.dp)
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
                    .align(Alignment.TopStart)
                    .size(iconSizeDp.dp),
            painter = painterResource(id = iconId),
            contentDescription = null,
        )


        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .background(MooiTheme.colorScheme.secondary, CircleShape)
                    .sizeIn(minWidth = 16.dp, minHeight = 16.dp)
                    .padding(vertical = 0.5.dp, horizontal = 2.dp),
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
                        letterSpacing = (-0.02).em,
                        color = Color.White,
                    ),
                maxLines = 1,
            )
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
                iconId = R.drawable.key,
                count = 3,
            )

            IconWithCount(
                modifier = Modifier.size(32.dp),
                iconId = R.drawable.key,
                count = 100,
            )
        }
    }
}
