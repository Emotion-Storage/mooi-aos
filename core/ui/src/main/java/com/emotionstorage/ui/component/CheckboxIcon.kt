package com.emotionstorage.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R

@Composable
fun CheckboxIcon(
    modifier: Modifier = Modifier.size(18.dp),
    enabled: Boolean = true,
    isSelected: Boolean = false,
    onSelect: () -> Unit = {},
    contentDescription: String? = null
) {
    Image(
        modifier = modifier.clickable(
            enabled = enabled,
            onClick = onSelect
        ),
        painter = painterResource(
            if (isSelected) R.drawable.checkbox_on else R.drawable.checkbox_off
        ),
        contentDescription = contentDescription
    )
}

@Preview
@Composable
private fun CheckboxIconPreview() {
    val (isSelected, setIsSelected) = remember { mutableStateOf(false) }
    CheckboxIcon(
        isSelected = isSelected,
        onSelect = { setIsSelected(!isSelected) }
    )
}