package com.emotionstorage.my.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MyPageScreen(
    modifier: Modifier = Modifier
) {
    StatelessMyPageScreen(modifier)
}

@Composable
private fun StatelessMyPageScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(modifier) { it ->
        Column {
            Text(
                modifier = Modifier.padding(it), text = "마이페이지 화면"
            )
            Button(
                onClick = {
                    // todo: logout
                }
            ) {
                Text("로그아웃")
            }
        }
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    StatelessMyPageScreen()
}