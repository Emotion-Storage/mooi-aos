package com.emotionstorage.data.model

import java.time.LocalDateTime

data class UserEntity(
    val socialType: AuthProvider,
    val socialId: String,
    val email: String,
    val name: String,
    val profileImageUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    enum class AuthProvider {
        GOOGLE,
        KAKAO,
    }
}
