package com.emotionstorage.auth.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.auth.R
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SocialLoginButton(
    provider: AuthProvider,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier =
            modifier
                .fillMaxWidth()
                .height(64.dp),
        shape = RoundedCornerShape(15.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
        onClick = onClick,
        colors =
            ButtonDefaults.buttonColors(
                containerColor =
                    when (provider) {
                        AuthProvider.KAKAO -> Color(0xFFFEE502)
                        AuthProvider.GOOGLE -> Color.White
                    },
                contentColor = Color(0xFF1A1802),
            ),
        contentPadding = PaddingValues(0.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier =
                    Modifier
                        .align(Alignment.CenterStart)
                        .height(21.dp)
                        .padding(start = 24.dp),
                painter =
                    painterResource(
                        when (provider) {
                            AuthProvider.KAKAO -> R.drawable.kakao_logo
                            AuthProvider.GOOGLE -> R.drawable.google_logo
                        },
                    ),
                contentDescription =
                    when (provider) {
                        AuthProvider.KAKAO -> stringResource(R.string.login_btn_kako)
                        AuthProvider.GOOGLE -> stringResource(R.string.login_btn_google)
                    },
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                style = MooiTheme.typography.mainButton,
                text =
                    if (provider == AuthProvider.KAKAO) {
                        stringResource(R.string.login_btn_kako)
                    } else {
                        stringResource(R.string.login_btn_google)
                    },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SocialLoginButtonPreview() {
    MooiTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SocialLoginButton(provider = AuthProvider.KAKAO, onClick = {})
            SocialLoginButton(provider = AuthProvider.GOOGLE, onClick = {})
        }
    }
}
