package com.emotionstorage.auth.domain.model

import java.time.LocalDate

// Data input from on user boarding precess
data class SignupForm (
    val idToken: String,
    val nickname: String,
    val gender: GENDER,
    val birthday: LocalDate,
    val expectations: List<String>,
    val isTermAgreed: Boolean,
    val isPrivacyAgreed: Boolean,
    val isMarketingAgreed: Boolean,
){
    enum class GENDER{
        MALE, FEMALE
    }
}