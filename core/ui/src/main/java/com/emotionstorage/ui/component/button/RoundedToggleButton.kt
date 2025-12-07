package com.emotionstorage.ui.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.subBackground

enum class RoundedToggleButtonType {
    FAVORITE,
}

@Composable
fun RoundedToggleButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {},
    contentDescription: String? = null,
    size: Int = 36,
    type: RoundedToggleButtonType = RoundedToggleButtonType.FAVORITE,
) {
    Box(
        modifier =
            modifier
                .size(size.dp)
                .subBackground(true, CircleShape)
                .clip(CircleShape)
                .clickable(
                    enabled = enabled,
                    onClick = onSelect,
                ),
    ) {
        if (type == RoundedToggleButtonType.FAVORITE) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(18.dp),
                painter =
                    painterResource(
                        if (isSelected) R.drawable.ic_favorite_filled else R.drawable.ic_favorite,
                    ),
                contentScale = ContentScale.Fit,
                contentDescription = contentDescription,
            )
        }
    }
}

@Preview
@Composable
private fun RoundedToggleButtonPreview() {
    Column(
        modifier = Modifier.background(MooiTheme.colorScheme.backgroundDefault)
    ) {
        RoundedToggleButton(
            isSelected = true,
        )
        RoundedToggleButton(
            isSelected = false,
        )
    }
}
