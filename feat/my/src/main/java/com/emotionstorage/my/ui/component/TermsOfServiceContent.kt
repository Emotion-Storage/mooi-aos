package com.emotionstorage.my.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.my.R

@Composable
fun TermsOfServiceContent() {
    val context = LocalContext.current
    val titles = context.resources.getStringArray(R.array.terms_titles)
    val contents = context.resources.getStringArray(R.array.terms_contents)

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
        contentPadding = PaddingValues(vertical = 18.dp),
    ) {
        itemsIndexed(titles) { index, title ->
            TermSection(
                title = title,
                content = contents[index],
            )

            if (index < titles.size - 1) Spacer(modifier = Modifier.size(32.dp))
        }

        item {
            Spacer(modifier = Modifier.size(12.dp))
            TermSection(
                title = stringResource(id = R.string.sub_terms_title),
                content = stringResource(id = R.string.sub_terms_content),
            )
        }
    }
}

@Composable
fun TermSection(
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
private fun TermsOfServiceContentPreview() {
    MooiTheme {
        TermsOfServiceContent()
    }
}
