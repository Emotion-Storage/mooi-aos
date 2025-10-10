package com.emotionstorage.remote.response.myPage

import com.emotionstorage.domain.model.AccountInfo
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Serializable
data class AccountInfoResponse(
    val email: String,
    val socialType: String,
    val gender: String,
    val birthday: String,
)

fun AccountInfoResponse.toDomain(): AccountInfo {
    val date = LocalDate.parse(birthday, DateTimeFormatter.ISO_DATE)

    return AccountInfo(
        email = email,
        socialType = socialType,
        gender = gender,
        birthYear = date.year,
        birthMonth = date.monthValue,
        birthDay = date.dayOfMonth,
    )
}
