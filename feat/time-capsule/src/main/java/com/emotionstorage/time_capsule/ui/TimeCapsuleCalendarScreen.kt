package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TimeCapsuleCalendarScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier) { it ->
        Text(
            modifier = Modifier.padding(it), text = "타임캡슐 캘린더 화면"
        )
    }
}