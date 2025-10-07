package com.emotionstorage.remote.response.myPage

import com.emotionstorage.domain.model.AccountInfo
import kotlinx.serialization.Serializable

@Serializable
data class AccountInfoResponse(
    val email: String,
    val socialType: String,
    val gender: String,
    val birthday: String,
)

fun AccountInfoResponse.toDomain(): AccountInfo {
    return AccountInfo(
        email = email,
        socialType = socialType,
        gender = gender,
        birthYear = birthday.split("-")[0].toInt(),
        birthMonth = birthday.split("-")[1].toInt(),
        birthDay = birthday.split("-")[2].toInt(),
    )
}
