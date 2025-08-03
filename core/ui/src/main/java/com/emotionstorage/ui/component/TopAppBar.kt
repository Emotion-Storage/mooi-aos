package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TopAppBar(
    showBackButton: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxWidth()
            .height(91.dp)
            .padding(bottom = 24.dp)
            .padding(horizontal = 16.dp)
    ) {
        if (showBackButton) {
            Image(
                painter = painterResource(id = R.drawable.arrow_back),
                modifier = Modifier
                    .width(11.dp)
                    .height(24.dp)
                    .align(Alignment.BottomStart),
                contentDescription = "back"
            )
        }
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    MooiTheme {
        TopAppBar(showBackButton = true)
    }
}