package com.emotionstorage.my.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val WS = "[\\s\\u00A0]"

private val numberLine = Regex("^$WS*\\(?([0-9]+)[\\.|\\)]\\)?$WS+")

private val bulletLine = Regex("^$WS*[•\\-\\u00B7\\u30FB\\u2219\\u2027\\u25CF]$WS+")

@Composable
fun RichBody(
    text: String,
    style: TextStyle =
        TextStyle(
            fontSize = 13.sp,
            lineHeight = 22.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
    color: Color = Color.White,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        text
            .lines()
            .map { it.replace('\u00A0', ' ').trimEnd() }
            .filter { it.isNotBlank() }
            .forEach { raw ->
                val line = raw.trimStart()
                when {
                    numberLine.containsMatchIn(line) -> {
                        val m = numberLine.find(line)!!
                        val idx = m.groupValues[1].toInt()
                        val body = line.substring(m.range.last + 1).trimStart()
                        NumberedListItem(index = idx, text = body, style = style, color = color)
                    }

                    bulletLine.containsMatchIn(line) -> {
                        val body = line.replace(bulletLine, "").trimStart()
                        BulletListItem(text = body, style = style, color = color)
                    }

                    else -> Text(line, style = style, color = color)
                }
            }
    }
}

@Composable
fun RichList(
    items: List<String>,
    style: ListStyle,
    textStyle: TextStyle =
        TextStyle(
            fontSize = 13.sp,
            lineHeight = 22.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false),
        ),
    color: Color = Color.White,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        when (style) {
            ListStyle.Bulleted -> items.forEach { BulletListItem(it, style = textStyle, color = color) }
            ListStyle.Numbered ->
                items.forEachIndexed { i, s ->
                    NumberedListItem(index = i + 1, text = s, style = textStyle, color = color)
                }
        }
    }
}
