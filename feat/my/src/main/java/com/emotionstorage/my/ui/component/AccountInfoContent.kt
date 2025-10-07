package com.emotionstorage.my.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.my.presentation.AuthProvider
import com.emotionstorage.ui.theme.MooiTheme


enum class PillStyle { Start, Center }

@Composable
fun AccountInfoContent(
    modifier: Modifier = Modifier,
    email: String,
    socialType: AuthProvider,
    gender: String,
    birthYear: String,
    birthMonth: String,
    birthDay: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 내 계정 (트레일링 아이콘이 있으면 넣고, 없으면 null)
        LabeledSection(label = "내 계정") {
            Pill(
                text = email,
                style = PillStyle.Start,
                trailing = {
                    when (socialType) {
                        AuthProvider.GOOGLE -> {

                        }

                        AuthProvider.KAKAO -> {

                        }
                    }
                }
            )
        }

        LabeledSection(label = "성별") {
            Pill(
                text = gender, style = PillStyle.Center,
                modifier = Modifier.widthIn(84.dp)
            )
        }

        LabeledSection(label = "생년월일") {
            Row {
                Pill(
                    modifier = Modifier.widthIn(98.dp),
                    text = birthYear,
                    style = PillStyle.Center
                )
                Spacer(Modifier.width(10.dp))
                Pill(
                    modifier = Modifier.widthIn(63.dp),
                    text = birthMonth,
                    style = PillStyle.Center
                )
                Spacer(Modifier.width(10.dp))
                Pill(
                    modifier = Modifier.widthIn(73.dp),
                    text = birthDay,
                    style = PillStyle.Center
                )
            }
        }
    }
}

@Composable
fun LabeledSection(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier) {
        Text(
            text = label,
            style = MooiTheme.typography.body8,
            color = Color.White,
        )
        Spacer(modifier = Modifier.size(12.dp))
        content()
    }
}

@Composable
fun Pill(
    modifier: Modifier = Modifier,
    text: String,
    style: PillStyle = PillStyle.Start,
    trailing: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MooiTheme.colorScheme.gray800
    ) {
        when (style) {
            PillStyle.Start -> {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text, style = MooiTheme.typography.body8, color = MooiTheme.colorScheme.gray500)
                    Spacer(Modifier.weight(1f))
                    trailing?.invoke()
                }
            }

            PillStyle.Center -> {
                Box(
                    Modifier
                        .height(50.dp)
                        .padding(horizontal = 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text, style = MooiTheme.typography.body8, color = MooiTheme.colorScheme.gray500)
                }
            }
        }
    }
}

@Preview
@Composable
fun AccountInfoContentPreview() {
    MooiTheme {
        AccountInfoContent(
            modifier = Modifier,
            email = "mooi.reply@gmail.com",
            socialType = AuthProvider.GOOGLE,
            gender = "남성",
            birthYear = "1999년",
            birthMonth = "7월",
            birthDay = "30일"
        )
    }
}
