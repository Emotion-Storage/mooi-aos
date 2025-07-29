package com.emotionstorage.ui.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun buildHighlightAnnotatedString(
    fullString: String,
    highlightWords: List<String>,
    highlightStyle: SpanStyle
): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0

        val regex = highlightWords.joinToString(separator = "|") { Regex.escape(it) }.toRegex()
        val matches = regex.findAll(fullString)

        for (match in matches) {
            val start = match.range.first
            val end = match.range.last + 1
            if (currentIndex < start) {
                append(fullString.substring(currentIndex, start))
            }
            withStyle(style = highlightStyle) {
                append(fullString.substring(start, end))
            }
            currentIndex = end
        }
        if (currentIndex < fullString.length) {
            append(fullString.substring(currentIndex))
        }
    }
}
