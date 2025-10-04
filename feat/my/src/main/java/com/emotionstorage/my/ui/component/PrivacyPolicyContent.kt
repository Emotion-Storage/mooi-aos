package com.emotionstorage.my.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.sp
import com.emotionstorage.my.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun PrivacyPolicyContent() {
    val context = LocalContext.current
    val titles = context.resources.getStringArray(R.array.privacy_titles)
    val contents = context.resources.getStringArray(R.array.privacy_contents)
    val tableHeaders = context.resources.getStringArray(R.array.privacy_table_headers).toList()
    val tableRow1 = context.resources.getStringArray(R.array.privacy_table_1th_row).toList()
    val tableRow2 = context.resources.getStringArray(R.array.privacy_table_2th_row).toList()
    val tableRow3 = context.resources.getStringArray(R.array.privacy_table_3th_row).toList()
    val rows = listOf(tableRow1, tableRow2, tableRow3)

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
    ) {
        item {
            Text(
                text = stringResource(id = R.string.privacy_main),
                style = MooiTheme.typography.caption1.copy(lineHeight = 22.sp),
                color = Color.White,
            )
        }

        item {
            Spacer(modifier = Modifier.size(32.dp))
            PrivacyPolicySection(
                title = stringResource(id = R.string.first_privacy_title),
                content = stringResource(id = R.string.first_privacy_content),
            )
        }

        item {
            Spacer(modifier = Modifier.size(18.dp))
            PrivacyTable(
                header = tableHeaders,
                rows = rows,
            )
            Spacer(modifier = Modifier.size(18.dp))
        }

        itemsIndexed(titles) { index, title ->
            PrivacyPolicySection(
                title = title,
                content = contents[index],
            )

            if (index < titles.size - 1) Spacer(modifier = Modifier.size(32.dp))
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PrivacyTable(
    header: List<String>,
    rows: List<List<String>>,
) {
    val baseTableW = 328f
    val baseColDp = listOf(76.5f, 108f, 67f, 76.6f)
    val baseRowDp = listOf(56f, 84f, 68f, 68f)

    val weights = baseColDp.map { it / baseTableW }

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val tableW = maxWidth
        // 화면 별 비율 측정
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
            HorizontalDivider(color = MooiTheme.colorScheme.gray700, thickness = divider)
            TableRow(items = rows[2], weights = weights, height = rowHeights[3])
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
                .requiredHeight(height)
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
                            MooiTheme.typography.body6.copy(
                                fontSize = 12.sp,
                                lineHeight = 18.sp,
                            )
                        } else {
                            MooiTheme.typography.caption5.copy(
                                fontSize = 12.sp,
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

@Composable
fun PrivacyPolicySection(
    title: String,
    content: String,
) {
    Column {
        Text(
            text = title,
            style = MooiTheme.typography.caption1.copy(lineHeight = 22.sp),
            color = Color.White,
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = content,
            style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
            color = Color.White,
        )
    }
}

@Preview
@Composable
fun PrivacyPolicyContentPreview() {
    MooiTheme {
        PrivacyPolicyContent()
    }
}
