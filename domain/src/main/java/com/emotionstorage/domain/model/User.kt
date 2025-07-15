package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class User(
    val id: String,
    val socialType: AuthProvider,
    val socialId: String,

    val email: String,
    val name: String,
    val profileImageUrl: String? = null,

    val accessToken:String,
    val refreshToken:String,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
