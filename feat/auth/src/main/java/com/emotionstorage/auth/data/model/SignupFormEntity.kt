package com.emotionstorage.auth.data.model

import java.time.LocalDate

data class SignupFormEntity (
    val idToken: String,
    val nickname: String,
    val gender: String,
    val birthday: LocalDate,
    val expectations: List<String>,
    val isTermAgreed: Boolean,
    val isPrivacyAgreed: Boolean,
    val isMarketingAgreed: Boolean,
)