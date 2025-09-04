package com.emotionstorage.my.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MyScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier) { it ->
        Text(
            modifier = Modifier.padding(it), text = "마이페이지 화면"
        )
    }
}