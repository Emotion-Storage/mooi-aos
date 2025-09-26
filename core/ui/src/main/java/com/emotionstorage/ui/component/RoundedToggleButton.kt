package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.util.subBackground

enum class RoundedToggleButtonType {
    FAVORITE,
}

@Composable
fun RoundedToggleButton(
    modifier: Modifier = Modifier.size(36.dp),
    enabled: Boolean = true,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {},
    contentDescription: String? = null,
    type: RoundedToggleButtonType = RoundedToggleButtonType.FAVORITE,
) {
    Box(
        modifier =
            modifier
                .subBackground(true, CircleShape)
                .clickable(
                    enabled = enabled,
                    onClick = onSelect,
                )
                .padding(3.dp),
    ) {
        if (type == RoundedToggleButtonType.FAVORITE) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .width(15.dp)
                        .height(16.dp),
                painter =
                    painterResource(
                        if (isSelected) R.drawable.favorite_filled else R.drawable.favorite,
                    ),
                contentDescription = contentDescription,
            )
        }
    }
}

@Preview
@Composable
private fun RoundedToggleButtonPreview() {
    val (isSelected, setIsSelected) = remember { mutableStateOf(false) }

    RoundedToggleButton(
        isSelected = isSelected,
        onSelect = { setIsSelected(!isSelected) },
    )
}
