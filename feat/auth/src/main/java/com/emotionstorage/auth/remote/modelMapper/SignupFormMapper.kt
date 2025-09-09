package com.emotionstorage.auth.remote.modelMapper

import com.emotionstorage.auth.data.model.SignupFormEntity
import com.emotionstorage.auth.remote.request.GoogleSignupRequestBody
import com.emotionstorage.auth.remote.request.KakaoSignupRequestBody
import java.time.format.DateTimeFormatter

internal object GoogleSignupFormMapper {
    fun toRemote(signupFormEntity: SignupFormEntity) = GoogleSignupRequestBody(
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

internal object KakaoSignupFormMapper {
    fun toRemote(signupFormEntity: SignupFormEntity) = KakaoSignupRequestBody(
        accessToken = signupFormEntity.idToken,
        nickname = signupFormEntity.nickname,
        gender = signupFormEntity.gender,
        birthday = signupFormEntity.birthday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        expectations = signupFormEntity.expectations,
        isTermsAgreed = signupFormEntity.isTermAgreed,
        isPrivacyAgreed = signupFormEntity.isPrivacyAgreed,
        isMarketingAgreed = signupFormEntity.isMarketingAgreed
    )
}