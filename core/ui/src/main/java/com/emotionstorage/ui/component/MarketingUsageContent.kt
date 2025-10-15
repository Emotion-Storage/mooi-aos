package com.emotionstorage.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.R

@Composable
fun MarketingUsageContent() {
    val context = LocalContext.current
    val tableHeaders = context.resources.getStringArray(R.array.marketing_table_headers).toList()
    val row1 = context.resources.getStringArray(R.array.marketing_table_1th_row).toList()
    val row2 = context.resources.getStringArray(R.array.marketing_table_2th_row).toList()
    val rows = listOf(row1, row2)

    Column(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 22.dp),
    ) {
        Text(
            text = "MOOI 마케팅 활용 및 수신동의",
            style = MooiTheme.typography.head1,
            color = Color.White,
        )

        Spacer(modifier = Modifier.size(22.dp))

        Text(
            text = stringResource(R.string.marketing_main),
            style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
            color = Color.White,
        )

        Spacer(modifier = Modifier.size(32.dp))

        MarketingTable(tableHeaders, rows)
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MarketingTable(
    header: List<String>,
    rows: List<List<String>>,
) {
    val baseTableW = 328f
    val baseColDp = listOf(84.5f, 53f, 51f, 84.5f, 55f)
    val baseRowDp = listOf(56f, 100f, 116f)

    val weights = baseColDp.map { it / baseTableW }

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val tableW = maxWidth
        val scale = tableW / baseTableW.dp

        val border = 1.dp
        val divider = 1.dp

        val rowHeights = baseRowDp.map { it.dp * scale }

        Column(
            modifier =
                Modifier
                    .width(tableW)
                    .border(border, MooiTheme.colorScheme.gray700, RoundedCornerShape(5.dp))
                    .background(MooiTheme.colorScheme.background),
        ) {
            TableRow(
                items = header,
                weights = weights,
                height = rowHeights[0],
                isHeader = true,
            )

            HorizontalDivider(color = MooiTheme.colorScheme.gray700, thickness = divider)
            TableRow(items = rows[0], weights = weights, height = rowHeights[1])
            HorizontalDivider(color = MooiTheme.colorScheme.gray700, thickness = divider)
            TableRow(items = rows[1], weights = weights, height = rowHeights[2])
        }
    }
}

@Composable
private fun TableRow(
    items: List<String>,
    weights: List<Float>,
    height: Dp,
    isHeader: Boolean = false,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .heightIn(min = height)
                .background(if (isHeader) MooiTheme.colorScheme.gray800 else Color.Transparent),
        verticalAlignment = if (isHeader) Alignment.CenterVertically else Alignment.Top,
    ) {
        items.forEachIndexed { index, text ->
            Box(
                Modifier
                    .weight(weights[index])
                    .fillMaxHeight()
                    .padding(12.dp)
                    .align(Alignment.CenterVertically),
                contentAlignment = if (isHeader) Alignment.Center else Alignment.TopStart,
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    style =
                        if (isHeader) {
                            MooiTheme.typography.caption1.copy(
                                fontSize = 12.sp,
                                lineHeight = (1.3).em,
                            )
                        } else {
                            MooiTheme.typography.caption7.copy(
                                fontSize = 12.sp,
                                lineHeight = (1.3).em,
                            )
                        },
                    textAlign = if (isHeader) TextAlign.Center else TextAlign.Start,
                    lineHeight = 18.sp,
                )
            }
            if (index < items.lastIndex) {
                VerticalDivider(
                    color = MooiTheme.colorScheme.gray700,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxHeight(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun MarketingUsageContentPreview() {
    MooiTheme {
        MarketingUsageContent()
    }
}
