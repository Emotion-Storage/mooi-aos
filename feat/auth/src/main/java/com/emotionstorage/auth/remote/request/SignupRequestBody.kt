package com.emotionstorage.auth.remote.request

import com.emotionstorage.common.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class SignupRequestBody (
    val idToken: String,
    val nickname: String,
    val gender: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate,
    val expectations: List<String>,
    val isTermAgreed: Boolean,
    val isPrivacyAgreed: Boolean,
    val isMarketingAgreed: Boolean,
)