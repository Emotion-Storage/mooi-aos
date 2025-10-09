package com.emotionstorage.domain.model

data class AccountInfo(
    val email: String,
    val socialType: String,
    val gender: String,
    val birthYear: Int,
    val birthMonth: Int,
    val birthDay: Int,
)
