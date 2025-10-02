package com.emotionstorage.my.ui.component

import android.widget.Space
import androidx.appcompat.widget.DialogTitle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.my.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun PrivacyPolicyContent() {
    val context = LocalContext.current
    val titles = context.resources.getStringArray(R.array.privacy_titles)
    val contents = context.resources.getStringArray(R.array.privacy_contents)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 18.dp)
    ) {

        item {
            Text(
                text = stringResource(id = R.string.privacy_main),
                style = MooiTheme.typography.caption1.copy(lineHeight = 22.sp),
                color = Color.White
            )
        }

        item {
            Spacer(modifier = Modifier.size(12.dp))
            PrivacyPolicySection(
                title = stringResource(id = R.string.first_privacy_title),
                content = stringResource(id = R.string.first_privacy_content)
            )
        }

        item { PrivacyTable() }

        itemsIndexed(titles) { index, title ->
            PrivacyPolicySection(
                title = title,
                content = contents[index]
            )

            if (index < titles.size - 1) Spacer(modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
fun PrivacyTable() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = MooiTheme.colorScheme.gray700,
                shape = RoundedCornerShape(5.dp)
            )
    ) {
        TableRow(
            items = listOf("개인정보", "필수항목", "선택항목", "보유 및 이용기간"),
            isHeader = true
        )
    }
}

@Composable
fun TableRow(
    items: List<String>,
    isHeader: Boolean = false,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isHeader) MooiTheme.colorScheme.gray800
                else Color.Transparent
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        items.forEachIndexed { index, item ->
            Text(
                text = item,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
                    .align(Alignment.CenterVertically),
                style = if (isHeader) MooiTheme.typography.body6
                else MooiTheme.typography.caption5,
                color = Color.White,
                fontSize = 12.sp,
            )
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
            color = Color.White
        )

        Spacer(modifier = Modifier.size(8.dp))

        Text(
            text = content,
            style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
            color = Color.White
        )
    }
}

@Preview
@Composable
fun TableRowPreview() {
    MooiTheme {
        TableRow(
            items = listOf("개인정보", "필수항목", "선택항목", "보유 및 이용기간"),
            isHeader = true
        )
    }

}

@Preview
@Composable
fun PrivacyTablePreview() {
    MooiTheme {
        PrivacyTable()
    }
}

@Preview
@Composable
fun PrivacyPolicyContentPreview() {
    MooiTheme {
        PrivacyPolicyContent()
    }
}
