package com.emotionstorage.my.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

enum class ClickArea { Row, Text, Icon, None }

@Composable
fun MenuSection(
    versionInfo: String,
    onAccountInfoClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onEmailCopyClick: () -> Unit = {},
    onTermsAndPrivacyClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentSize()
                .background(
                    color = MooiTheme.colorScheme.chatMessageColor,
                    shape = RoundedCornerShape(10.dp),
                ),
    ) {
        MenuItem(
            title = "계정 정보",
            clickArea = ClickArea.Row,
            onClick = onAccountInfoClick,
        )

        MenuItem(
            title = "알림 설정",
            clickArea = ClickArea.Row,
            onClick = onNotificationClick,
        )

        Spacer(modifier = Modifier.size(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.size(8.dp))

        EmailMenuItem(
            title = "MOOI에게 의견 보내기",
            email = "(mooi.reply@gmail.com)",
            onCopyClick = {
                onEmailCopyClick()
            },
        )

        MenuItem(
            title = "이용약관 및 개인정보처리방침",
            clickArea = ClickArea.Row,
            onClick = onTermsAndPrivacyClick,
        )

        Spacer(modifier = Modifier.size(8.dp))
        DividerLine()
        Spacer(modifier = Modifier.size(8.dp))

        MenuItem(
            title = "버전정보",
            trailingText = versionInfo,
            showArrow = false,
            clickArea = ClickArea.None,
        )

        MenuItem(
            title = "로그아웃",
            showArrow = false,
            trailingText = null,
            clickArea = ClickArea.Text,
            onClick = onLogoutClick,
        )
    }
}

@Composable
fun MenuItem(
    title: String,
    showArrow: Boolean = true,
    trailingText: String? = null,
    clickArea: ClickArea = ClickArea.Icon,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .fillMaxWidth()
                .let {
                    if (clickArea == ClickArea.Row) it.clickable { onClick() } else it
                },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val textModifier =
            if (clickArea == ClickArea.Text) {
                Modifier
                    .wrapContentWidth()
                    .clickable { onClick() }
            } else {
                Modifier.weight(1f)
            }

        Text(
            modifier = textModifier,
            text = title,
            style = MooiTheme.typography.body7,
            color = MooiTheme.colorScheme.gray300,
        )

        if (trailingText != null) {
            Text(
                text = trailingText,
                style = MooiTheme.typography.body8,
                color = MooiTheme.colorScheme.gray300,
            )
        }

        if (showArrow) {
            Image(
                painter = painterResource(R.drawable.arrow_front),
                contentDescription = "상세 보기",
            )
        }
    }
}

@Composable
fun EmailMenuItem(
    title: String,
    email: String = "(mooi.reply@gmail.com)",
    onCopyClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .padding(horizontal = 18.dp, vertical = 16.dp)
                .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                style = MooiTheme.typography.body7,
                color = MooiTheme.colorScheme.gray300,
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = email,
                style = MooiTheme.typography.caption7,
                color = MooiTheme.colorScheme.gray300,
            )
        }

        Image(
            painterResource(R.drawable.clipboard),
            contentDescription = "복사",
            modifier =
                Modifier
                    .align(Alignment.Bottom)
                    .offset(x = 0.dp, y = (-6).dp)
                    .clickable {
                        onCopyClick()
                    },
        )
    }
}

@Composable
fun DividerLine() {
    HorizontalDivider(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        thickness = 1.5.dp,
        color = MooiTheme.colorScheme.gray800,
    )
}

@Preview
@Composable
fun MenuItemPreview() {
    MooiTheme {
        MenuItem(
            title = "계정 정보",
            showArrow = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
fun EmailItemPreview() {
    MooiTheme {
        EmailMenuItem(
            title = "MOOI에게 의견 보내기",
            email = "(mooi.reply@gmail.com)",
            onCopyClick = {
            },
        )
    }
}

@Preview
@Composable
fun DividerLinePreview() {
    MooiTheme {
        DividerLine()
    }
}

@Preview
@Composable
fun MenuSectionPreview() {
    MooiTheme {
        MenuSection(
            versionInfo = "v1.0.0",
        )
    }
}
