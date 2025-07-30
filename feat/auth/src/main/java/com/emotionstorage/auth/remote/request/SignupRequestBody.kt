package com.emotionstorage.auth.remote.request

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SignupRequestBody (
    val idToken: String,
    val nickname: String,
    val gender: String,
    val birthday: LocalDate,
    val isTermAgreed: Boolean,
    val isPrivacyAgreed: Boolean,
    val isMarketingAgreed: Boolean,
)