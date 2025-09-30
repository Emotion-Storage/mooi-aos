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
import androidx.compose.foundation.lazy.LazyListState
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

@Composable
fun WheelSpinner(
    items: List<String>,
    modifier: Modifier = Modifier,
    selectedItem: String? = null,
    onItemSelect: (String) -> Unit = {},
    visibleItemsCount: Int = 5,
    showCenterIndicator: Boolean = true,
) {
    require(visibleItemsCount % 2 == 1) { "WheelSpinner: visibleItemsCount는 홀수여야 합니다." }
    val centerIndex = visibleItemsCount / 2
    val wheelItems = remember(items) { List(centerIndex) { "" } + items + List(centerIndex) { "" } }
    // initial scroll position - center selected item
    val initialIndex =
        remember(key1 = selectedItem, key2 = items) {
            items.indexOf(selectedItem).takeIf { it != -1 } ?: 0
        }
    val listState =
        rememberLazyListState(
            initialFirstVisibleItemIndex = initialIndex,
        )

    // keep track of the last scroll index, to prevent multiple triggers
    val (lastSelectedIndex, setLastSelectedIndex) = remember(initialIndex) { mutableStateOf(initialIndex) }

    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val selectedIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex + centerIndex }
    }

    // trigger item selection when scroll finished
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            if (selectedIndex in wheelItems.indices && selectedIndex != lastSelectedIndex) {
                // trigger item selection
                if (wheelItems[selectedIndex].isNotEmpty()) onItemSelect(wheelItems[selectedIndex])
            }
        }
    }

    // scroll to selected item when selectedItem changes
    LaunchedEffect(key1 = selectedItem, key2 = items) {
        val target = selectedItem ?: return@LaunchedEffect
        val targetIndex = items.indexOf(target)
        if (targetIndex >= 0) {
            val centeredIndex = targetIndex + centerIndex
            if (centeredIndex != lastSelectedIndex) {
                listState.scrollToItem(targetIndex)
                setLastSelectedIndex(centeredIndex)
            }
        }
    }

    WheelSpinnerContent(
        modifier = modifier,
        listState = listState,
        wheelItems = wheelItems,
        firstVisibleItemIndex = firstVisibleItemIndex.value,
        centerIndex = centerIndex,
        showCenterIndicator = showCenterIndicator,
    )
}

@Composable
private fun WheelSpinnerContent(
    listState: LazyListState,
    wheelItems: List<String>,
    modifier: Modifier = Modifier,
    firstVisibleItemIndex: Int = 0,
    centerIndex: Int = 2,
    showCenterIndicator: Boolean = true,
) {
    Box(
        modifier =
            modifier
                .background(Color.Transparent),
    ) {
        if (showCenterIndicator) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .height(38.dp)
                        .fillMaxWidth()
                        .background(MooiTheme.colorScheme.dropBox, RoundedCornerShape(15.dp)),
            )
        }

        LazyColumn(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .height(168.dp)
                    .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState,
        ) {
            itemsIndexed(wheelItems) { index, item ->
                val isCenter = firstVisibleItemIndex + centerIndex == index
                val isAdjacent =
                    firstVisibleItemIndex + centerIndex - 1 == index || firstVisibleItemIndex + centerIndex + 1 == index

                val animatedColor by animateColorAsState(
                    if (isCenter) {
                        Color.White
                    } else if (isAdjacent) {
                        MooiTheme.colorScheme.gray400.copy(alpha = 0.8f)
                    } else {
                        MooiTheme.colorScheme.gray400.copy(alpha = 0.3f)
                    },
                )

                Box(
                    modifier =
                        Modifier
                            .height(24.dp)
                            .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = item,
                        style = MooiTheme.typography.body1,
                        color = animatedColor,
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
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
        ) {
            WheelSpinner(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .width(100.dp),
                items = items,
                selectedItem = selected,
                onItemSelect = { selected = it },
            )
        }
    }
}
