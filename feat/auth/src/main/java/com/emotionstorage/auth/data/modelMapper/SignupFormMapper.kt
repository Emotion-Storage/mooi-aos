package com.emotionstorage.auth.data.modelMapper

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.domain.model.SignupForm

internal object SignupFormMapper {
    fun toData(signupForm: SignupForm) = SignupFormEntity(
        idToken = signupForm.idToken ?: throw IllegalStateException("id token is null"),
        nickname = signupForm.nickname ?: throw IllegalStateException("nickname is null"),
        gender = signupForm.gender?.value ?: throw IllegalStateException("gender is null"),
        birthday = signupForm.birthday ?: throw IllegalStateException("birthday is null"),
        expectations = signupForm.expectations
            ?: throw IllegalStateException("expectations is null"),
        isTermAgreed = signupForm.isTermAgreed
            ?: throw IllegalStateException("isTermAgreed is null"),
        isPrivacyAgreed = signupForm.isPrivacyAgreed
            ?: throw IllegalStateException("isPrivacyAgreed is null"),
        isMarketingAgreed = signupForm.isMarketingAgreed
            ?: throw IllegalStateException("isMarketingAgreed is null")
    )
}