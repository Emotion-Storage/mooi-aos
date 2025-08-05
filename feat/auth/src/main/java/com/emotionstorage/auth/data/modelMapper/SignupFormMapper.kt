package com.emotionstorage.auth.data.modelMapper

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.domain.model.SignupForm

internal object SignupFormMapper {
    fun toData(signupForm: SignupForm) = SignupFormEntity(
        idToken = signupForm.idToken,
        nickname = signupForm.nickname,
        gender = when (signupForm.gender) {
            SignupForm.GENDER.MALE -> "male"
            SignupForm.GENDER.FEMALE -> "female"
            else -> ""
        },
        birthday = signupForm.birthday,
        expectations = signupForm.expectations,
        isTermAgreed = signupForm.isTermAgreed,
        isPrivacyAgreed = signupForm.isPrivacyAgreed,
        isMarketingAgreed = signupForm.isMarketingAgreed
    )
}