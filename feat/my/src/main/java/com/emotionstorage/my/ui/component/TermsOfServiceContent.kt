package com.emotionstorage.my.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.my.R
import com.emotionstorage.ui.theme.MooiTheme

enum class ListStyle { Bulleted, Numbered }

@Composable
fun TermsOfServiceContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp),
) {
    val titles = stringArrayResource(id = R.array.terms_titles)
    val contents = stringArrayResource(id = R.array.terms_contents)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        itemsIndexed(titles) { index, title ->
            val body = contents.getOrNull(index).orEmpty()

            val listItems =
                rememberOptionalStringArray(
                    name = "terms_section_${index + 1}_items",
                )

            TermSectionWithOptionalList(
                title = title,
                paragraph = body,
                listItems = listItems,
                listStyle = ListStyle.Numbered,
            )
        }
        item { Spacer(Modifier.size(40.dp)) }
    }
}

@Composable
private fun TermSectionWithOptionalList(
    title: String,
    paragraph: String?,
    listItems: List<String>?,
    listStyle: ListStyle = ListStyle.Numbered,
) {
    Text(
        text = title,
        style = MooiTheme.typography.caption1.copy(lineHeight = 22.sp),
        color = Color.White,
    )
    Spacer(Modifier.size(8.dp))
    if (!paragraph.isNullOrBlank()) RichBody(paragraph)
    if (!listItems.isNullOrEmpty()) {
        Spacer(Modifier.size(6.dp))
        RichList(items = listItems, style = listStyle)
    }
    Spacer(Modifier.size(12.dp))
}

@Composable
private fun rememberOptionalStringArray(name: String): List<String>? {
    val context = LocalContext.current
    return remember(name) {
        val id = context.resources.getIdentifier(name, "array", context.packageName)
        if (id != 0) context.resources.getStringArray(id).toList() else null
    }
}

@Preview
@Composable
private fun TermsOfServiceContentPreview() {
    MooiTheme {
        TermsOfServiceContent()
    }
}
