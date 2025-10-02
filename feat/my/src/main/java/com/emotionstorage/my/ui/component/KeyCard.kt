package com.emotionstorage.my.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun KeyCard(
    keyCount: Int,
    onCardClick: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(
                color = MooiTheme.colorScheme.blueGrayBackground,
            ),
        shape = RoundedCornerShape(10.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .background(
                    brush = MooiTheme.brushScheme.subButtonBackground
                )
                .border(
                    width = 1.dp,
                    brush = MooiTheme.brushScheme.subButtonBorder,
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(start = 16.dp, end = 16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.key_white),
                contentDescription = "열쇠",
                modifier = Modifier
                    .size(13.dp)
                    .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.size(6.dp))

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "보유한 열쇠",
                style = MooiTheme.typography.caption3,
                color = Color.White
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "${keyCount}개",
                style = MooiTheme.typography.caption3,
                color = MooiTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(6.dp))

            Image(
                painter = painterResource(R.drawable.arrow_front),
                contentDescription = "열쇠 갯수 확인",
                modifier = Modifier
                    .size(13.dp)
                    .align(Alignment.CenterVertically)
                    .clickable {
                        onCardClick(keyCount)
                    }
            )
        }
    }
}


@Preview
@Composable
fun KeyCardPreview() {
    MooiTheme {
        KeyCard(
            keyCount = 10,
            onCardClick = { key ->

            }
        )
    }
}
