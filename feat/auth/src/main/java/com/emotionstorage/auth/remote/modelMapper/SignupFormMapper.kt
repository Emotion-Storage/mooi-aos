package com.emotionstorage.auth.remote.modelMapper

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.remote.request.SignupRequestBody
import java.time.format.DateTimeFormatter

internal object SignupFormMapper {
    fun toRemote(signupFormEntity: SignupFormEntity) = SignupRequestBody(
        idToken = signupFormEntity.idToken,
        nickname = signupFormEntity.nickname,
        gender = signupFormEntity.gender,
        birthday = signupFormEntity.birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        expectations = signupFormEntity.expectations,
        isTermsAgreed = signupFormEntity.isTermAgreed,
        isPrivacyAgreed = signupFormEntity.isPrivacyAgreed,
        isMarketingAgreed = signupFormEntity.isMarketingAgreed
    )
}