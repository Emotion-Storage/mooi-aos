package com.emotionstorage.home.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("로그인 성공 - 홈 진입")
        Button(onClick = { /*TODO: 로그아웃*/ }) {
            Text("로그아웃")
        }
        Button(onClick = { /*TODO: 회원탈퇴*/ }) {
            Text("회원탈퇴")
        }
    }
}