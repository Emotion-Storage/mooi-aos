package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class AccountInfo(
    val nickname: String,
    val email: String,
    val socialType: String,
    val gender: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDay: Int,
)

fun AccountInfo.toUser() =  User(
    socialType = enumValueOf(socialType),
    email = email,
    nickname = nickname,
    profileImageUrl = null,
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now(),
)
