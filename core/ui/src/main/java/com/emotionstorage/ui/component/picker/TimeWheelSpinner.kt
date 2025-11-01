package com.emotionstorage.ui.component.picker

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalTime
import kotlin.math.roundToInt

@Composable
fun TimeWheelSpinner(
    selected: LocalTime,
    onSelect: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    val period = if (selected.hour >= 12) "오후" else "오전"
    val hour12 = ((selected.hour + 11) % 12) + 1

    val minuteItems = (0..55 step 5).toList()
    val selectedMinute = selected.minute

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 20.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PeriodWheel(
                modifier = Modifier.weight(1f),
                selected = period,
                onSelected = { newPeriod ->
                    val newHour24 = toHour24(newPeriod, hour12)
                    onSelect(selected.withHour(newHour24))
                },
            )

            Spacer(Modifier.width(12.dp))

            NumberWheel(
                modifier = Modifier.weight(1f),
                values = (1..12).toList(),
                selected = hour12,
                onSelected = { newHour12 ->
                    val newHour24 = toHour24(period, newHour12)
                    onSelect(selected.withHour(newHour24))
                },
                formatter = { it.toString() },
            )

            Text(
                text = " : ",
                style = MooiTheme.typography.body1,
                color = MooiTheme.colorScheme.gray400,
                modifier = Modifier.padding(horizontal = 4.dp),
            )

            NumberWheel(
                modifier = Modifier.weight(1f),
                values = minuteItems,
                selected = selectedMinute,
                onSelected = { newMinute ->
                    onSelect(
                        selected
                            .withMinute(newMinute)
                            .withSecond(0)
                            .withNano(0),
                    )
                },
                formatter = { it.toString().padStart(2, '0') },
            )
        }
    }
}

@Composable
private fun WheelList(
    items: List<String>,
    modifier: Modifier = Modifier,
    visibleItemsCount: Int = 3,
    itemHeight: Dp = 24.dp,
    itemSpacing: Dp = 12.dp,
    listState: LazyListState,
    onSnappedTo: (centerAbsIndex: Int) -> Unit = {},
) {
    require(visibleItemsCount % 2 == 1) { "visibleItemsCount must be odd." }
    val centerIndex = visibleItemsCount / 2
    val totalHeight = itemHeight * visibleItemsCount + itemSpacing * (visibleItemsCount - 1)
    val density = LocalDensity.current

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            val first = listState.firstVisibleItemIndex
            val offset = listState.firstVisibleItemScrollOffset
            val itemExtentPx = with(density) { (itemHeight + itemSpacing).toPx() }
            val delta = (offset / itemExtentPx).roundToInt()
            val snapFirst = first + delta
            if (snapFirst in items.indices) {
                listState.animateScrollToItem(snapFirst)
                onSnappedTo(snapFirst + centerIndex)
            }
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .height(totalHeight)
                    .fillMaxWidth(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(itemSpacing),
        ) {
            itemsIndexed(items) { index, item ->
                val isCenter = listState.firstVisibleItemIndex + centerIndex == index
                val isAdjacent =
                    listState.firstVisibleItemIndex + centerIndex - 1 == index ||
                        listState.firstVisibleItemIndex + centerIndex + 1 == index
                val animatedColor by animateColorAsState(
                    targetValue =
                        when {
                            isCenter -> Color.White
                            isAdjacent -> MooiTheme.colorScheme.gray500
                            else -> MooiTheme.colorScheme.gray500
                        },
                    label = "wheelColor",
                )

                Box(
                    Modifier
                        .height(itemHeight)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = item, color = animatedColor, style = MooiTheme.typography.body1)
                }
            }
        }
    }
}

@Composable
fun PeriodWheel(
    modifier: Modifier = Modifier,
    selected: String,
    onSelected: (String) -> Unit,
    periods: List<String> = listOf("오전", "오후"),
) {
    // 3칸으로 조절
    val visible = 3
    val centerIndex = visible / 2
    val wheelItems = remember(periods) { listOf("") + periods + listOf("") }

    val initial =
        remember(selected, periods) {
            val idx = periods.indexOf(selected).coerceAtLeast(0)
            idx + 1
        }
    val listState = rememberLazyListState(initial)

    LaunchedEffect(selected) {
        val idx = periods.indexOf(selected).coerceAtLeast(0)
        val target = idx + 1
        if (target != listState.firstVisibleItemIndex + centerIndex) {
            listState.animateScrollToItem(target - centerIndex)
        }
    }

    WheelList(
        items = wheelItems,
        modifier = modifier,
        visibleItemsCount = visible,
        listState = listState,
        onSnappedTo = { centerAbsIndex ->
            val actual = centerAbsIndex - 1
            if (actual in periods.indices) {
                val v = periods[actual]
                if (v != selected) onSelected(v)
            }
        },
    )
}

@Composable
fun NumberWheel(
    values: List<Int>,
    modifier: Modifier = Modifier,
    selected: Int,
    onSelected: (Int) -> Unit,
    visibleItemsCount: Int = 3,
    formatter: (Int) -> String = { it.toString() },
) {
    require(values.isNotEmpty())
    val repeat = 50
    val repeated = remember(values) { List(repeat) { values }.flatten() }

    val pad = visibleItemsCount / 2
    val wheelItems =
        remember(repeated, formatter) {
            List(pad) { "" } + repeated.map(formatter) + List(pad) { "" }
        }

    val base = (repeated.size / 2 / values.size) * values.size
    val selectedIdxInValues = values.indexOf(selected).coerceAtLeast(0)
    val initialInRepeated = base + selectedIdxInValues
    val listState = rememberLazyListState(initialInRepeated)

    LaunchedEffect(selected) {
        val targetBase = listState.firstVisibleItemIndex
        val currValIdx = targetBase % values.size
        val desiredIdxDelta = (selectedIdxInValues - currValIdx).mod(values.size)
        val target = targetBase + desiredIdxDelta
        listState.animateScrollToItem(target)
    }

    WheelList(
        items = wheelItems,
        modifier = modifier,
        visibleItemsCount = visibleItemsCount,
        listState = listState,
        onSnappedTo = { centerAbsIndex ->
            val real = centerAbsIndex - pad
            if (real in repeated.indices) {
                val v = repeated[real]
                if (v != selected) onSelected(v)
            }
        },
    )
}

private fun toHour24(
    period: String,
    hour12: Int,
): Int {
    val h = hour12 % 12
    return if (period == "오전") {
        if (h == 0) 0 else h
    } else {
        if (h == 0) 12 else h + 12
    }
}

@Preview
@Composable
private fun TimeWheelSpinnerPreview() {
    MooiTheme {
        TimeWheelSpinner(
            selected = LocalTime.now(),
            onSelect = {},
            modifier = Modifier.padding(10.dp),
        )
    }
}
