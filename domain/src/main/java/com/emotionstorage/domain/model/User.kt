package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class User(
    val id: String,
    val socialType: AuthProvider,
    val socialId: String,

    val email: String,
    val nickname: String,
    val profileImageUrl: String? = null,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
){
    enum class AuthProvider {
        GOOGLE, KAKAO
    }
}
