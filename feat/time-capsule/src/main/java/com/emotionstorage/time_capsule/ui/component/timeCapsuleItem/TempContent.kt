package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.errorRedBackground

@Composable
fun TempContent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            modifier
                .height(90.dp)
                .fillMaxWidth()
                .errorRedBackground(
                    true,
                    RoundedCornerShape(15.dp),
                ).clip(RoundedCornerShape(15.dp))
                .clickable(onClick = onClick)
                .padding(start = 15.dp, end = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                modifier = Modifier.height(24.dp),
                text = "아직 보관하지 않은 타임캡슐이 있어요.",
                style = MooiTheme.typography.caption3,
                color = MooiTheme.colorScheme.gray500,
                textAlign = TextAlign.Center,
            )
            Text(
                modifier = Modifier.height(24.dp),
                text = "이어서 보관하러 갈까요?",
                style = MooiTheme.typography.body1,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Image(
            modifier =
                Modifier
                    .size(11.dp, 24.dp)
                    .rotate(180f),
            painter = painterResource(id = R.drawable.arrow_back),
            contentDescription = "타임캡슐 보관하기",
        )
    }
}
