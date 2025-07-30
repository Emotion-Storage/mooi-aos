package com.emotionstorage.auth.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.auth.R

@Composable
fun SocialLoginButton(
    provider: AuthProvider,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    // todo: add social provider icon
    Button(
        modifier = modifier
            .width(328.dp)
            .height(64.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.elevation(0.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = when (provider) {
                AuthProvider.KAKAO -> Color(0xFFFEE502)
                AuthProvider.GOOGLE -> Color.White
            },
            contentColor = Color(0xFF1A1802)
        )
    ) {
        Text(
            style = MooiTheme.typography.button,
            text =
                if (provider == AuthProvider.KAKAO) stringResource(R.string.login_btn_kako)
                else stringResource(R.string.login_btn_google)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SocialLoginButtonPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SocialLoginButton(provider = AuthProvider.KAKAO, onClick = {})
            SocialLoginButton(provider = AuthProvider.GOOGLE, onClick = {})
        }
    }
}