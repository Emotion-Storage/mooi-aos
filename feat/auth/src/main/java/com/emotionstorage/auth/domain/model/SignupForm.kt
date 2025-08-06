package com.emotionstorage.auth.domain.model

import java.time.LocalDate

// Data input from on user boarding precess
data class SignupForm (
    val idToken: String? = null,
    val nickname: String? = null,
    val gender: GENDER? = null,
    val birthday: LocalDate? = null,
    val expectations: List<String>? = null,
    val isTermAgreed: Boolean? = null,
    val isPrivacyAgreed: Boolean? = null,
    val isMarketingAgreed: Boolean? = null,
){
    enum class GENDER(val value: String){
        MALE("male"), FEMALE("female")
    }
}