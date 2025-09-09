package com.emotionstorage.auth.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class KakaoSignupRequestBody (
    val accessToken: String,
    val nickname: String,
    val gender: String,
    val birthday: String,
    val expectations: List<String>,
    val isTermsAgreed: Boolean,
    val isPrivacyAgreed: Boolean,
    val isMarketingAgreed: Boolean,
)