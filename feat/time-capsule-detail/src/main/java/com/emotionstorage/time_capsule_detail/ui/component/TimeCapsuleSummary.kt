package com.emotionstorage.time_capsule_detail.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TimeCapsuleSummary(
    title: String,
    summary: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = title,
            style = MooiTheme.typography.body1,
            color = Color.White,
        )
        Box(
            modifier =
                Modifier
                    .background(Color(0x1AAECBFA), RoundedCornerShape(15.dp))
                    .padding(18.dp),
        ) {
            Text(
                text = summary,
                style = MooiTheme.typography.caption3,
                color = Color.White,
            )
        }
    }
}
