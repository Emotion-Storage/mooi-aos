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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

// TODO 상수 값 빼기
@Composable
fun ProfileHeader(
    profileImage: String,
    nickname: String = "찡찡이",
    signupDday: Int = 280,
    onProfileClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painterResource(R.drawable.emotion_0),
            contentDescription = "프로필 이미지",
            modifier =
                Modifier
                    .size(66.dp)
                    .clickable {
                        onProfileClick()
                    }
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape,
                    ),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.size(18.dp))

        Column(
            modifier =
                Modifier
                    .size(
                        width = 201.dp,
                        height = 60.dp
                    )
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
            ) {
                Text(
                    text = nickname + "님",
                    style = MooiTheme.typography.head2,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Edit",
                    style = MooiTheme.typography.body4.copy(
                        fontSize = 12.sp, lineHeight = 24.sp
                    ),
                    color = MooiTheme.colorScheme.secondary,
                    modifier = Modifier
                        .offset(x = 0.dp, y = (-5).dp)
                        .clickable {
                            onEditClick()
                        }
                )

                Spacer(modifier = Modifier.size(2.dp))

                Image(
                    painterResource(R.drawable.edit),
                    contentDescription = "수정",
                    modifier = Modifier
                        .size(8.dp)
                        .offset(x = 0.dp, y = (-2.5).dp)
                )
            }

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = MooiTheme.typography.caption4.toSpanStyle().copy(
                            color = MooiTheme.colorScheme.gray500
                        )
                    ) {
                        append("MOOI와 함께한 지 ")
                    }

                    withStyle(
                        style = MooiTheme.typography.caption4.toSpanStyle().copy(
                            color = MooiTheme.colorScheme.primary
                        )
                    ) {
                        append("${signupDday}일")
                    }

                    withStyle(
                        style = MooiTheme.typography.caption4.toSpanStyle().copy(
                            color = MooiTheme.colorScheme.gray500
                        )
                    ) {
                        append("째에요.")
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    MooiTheme {
        ProfileHeader(
            profileImage = "Glide or Coil이 필요해 보인다",
            nickname = "찡찡이",
            signupDday = 280
        )
    }
}
