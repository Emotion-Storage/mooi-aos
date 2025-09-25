package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
fun TopAppBar(
    modifier: Modifier = Modifier,
    showBackground: Boolean = true,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    title: String? = null,
    rightComponent: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.background)
                .background(if (showBackground) Color(0x800E0C12) else MooiTheme.colorScheme.background)
                .fillMaxWidth()
                .height(62.dp)
                .padding(horizontal = 16.dp),
    ) {
        if (showBackButton) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                modifier =
                    Modifier
                        .width(11.dp)
                        .height(24.dp)
                        .align(Alignment.CenterStart)
                        .clickable { onBackClick() },
                contentDescription = "back",
            )
        }
        if (title != null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MooiTheme.typography.body2,
                color = Color.White,
            )
        }
        if (rightComponent != null) {
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                rightComponent()
            }
        }
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    MooiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            TopAppBar(showBackButton = true)
            TopAppBar(
                showBackButton = true,
                title = "타임캡슐 상세",
                rightComponent = {
                    RoundedToggleButton()
                },
            )
        }
    }
}
