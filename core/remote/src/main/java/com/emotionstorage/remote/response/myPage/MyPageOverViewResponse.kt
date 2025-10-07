package com.emotionstorage.remote.response.myPage

import com.emotionstorage.domain.model.MyPage
import kotlinx.serialization.Serializable

@Serializable
data class MyPageOverViewResponse(
    val nickname: String,
    val days: Int,
    val keys: Int,
)

fun MyPageOverViewResponse.toDomainModel(): MyPage {
    return MyPage(
        nickname = nickname,
        days = days,
        keys = keys,
    )
}

