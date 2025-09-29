package com.emotionstorage.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.delay

@Composable
fun WheelSpinner(
    items: List<String>,
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    onItemSelect: (String) -> Unit = {},
    visibleItemsCount: Int = 5,
    showCenterLine: Boolean = true
) {
    val centerIndex = visibleItemsCount / 2
    val wheelItems = List(centerIndex) { "" } + items + List(centerIndex) { "" }

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex =
            if (selectedItem != null) wheelItems.indexOf(selectedItem) - centerIndex else centerIndex
    )
    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(listState.isScrollInProgress) {
        // select item on center, when scroll finished
        if (!listState.isScrollInProgress) {
            val selectedIndex = listState.firstVisibleItemIndex + centerIndex
            if (selectedIndex in wheelItems.indices) {
                // scroll to target index to align scroll position
                val targetIndex = (selectedIndex - centerIndex).coerceIn(0, wheelItems.size - 1)
                listState.animateScrollToItem(targetIndex)

                // wait for animation to finish
                delay(200)
                onItemSelect(wheelItems[selectedIndex])
            }
        }
    }

    Box(
        modifier = modifier
            .background(Color.Transparent)
    ) {
        if (showCenterLine) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(38.dp)
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.dropBox, RoundedCornerShape(15.dp)),
            )
        }

        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .height(168.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState,
        ) {
            itemsIndexed(wheelItems) { index, item ->
                val isCenter = firstVisibleItemIndex.value + centerIndex == index
                val isAdjacent =
                    firstVisibleItemIndex.value + centerIndex - 1 == index || firstVisibleItemIndex.value + centerIndex + 1 == index

                val animatedColor by animateColorAsState(
                    if (isCenter) Color.White
                    else if (isAdjacent) MooiTheme.colorScheme.gray400.copy(alpha = 0.8f)
                    else MooiTheme.colorScheme.gray400.copy(alpha = 0.3f),
                )

                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .fillMaxWidth(), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item, style = MooiTheme.typography.body1, color = animatedColor
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun PreviewWheelSpinner() {
    val items = (1..12).map { "${it}월" }
    var selected by remember { mutableStateOf<String?>("7월") }

    MooiTheme {
        Box(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            WheelSpinner(
                modifier = Modifier.align(Alignment.Center).width(100.dp),
                items = items,
                selectedItem = selected,
                onItemSelect = { selected = it })
        }
    }
}
