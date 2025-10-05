package com.emotionstorage.domain.model

import com.emotionstorage.domain.model.User.AuthProvider
import java.time.LocalDate

enum class Expectation(
    val content: String,
) {
    EMOTION("내 감정을 정리하고 싶어요"),
    STRESS("스트레스를 관리하고 싶어요"),
    REGRET("후회나 힘든 감정을 털어내고 싶어요"),
    MEMORY("좋은 기억을 오래 간직하고 싶어요"),
    PATTERN("내 감정 패턴을 알고 싶어요"),
    RECORD("그냥 편하게 내 얘기를 남기고 싶어요"),
}

// Data input from on user boarding precess
data class SignupForm(
    val provider: AuthProvider? = null,
    val idToken: String? = null,
    val nickname: String? = null,
    val gender: GENDER? = null,
    val birthday: LocalDate? = null,
    val expectations: List<Expectation>? = null,
    val isTermAgreed: Boolean? = null,
    val isPrivacyAgreed: Boolean? = null,
    val isMarketingAgreed: Boolean? = null,
) {
    enum class GENDER(
        val value: String,
    ) {
        MALE("MALE"),
        FEMALE("FEMALE"),
    }
}
