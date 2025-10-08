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

fun AccountInfoResponse.toDomain(): AccountInfo =
    AccountInfo(
        email = email,
        socialType = socialType,
        gender = gender,
        birthYear = birthday.split("-")[0].toInt(),
        birthMonth = birthday.split("-")[1].toInt(),
        birthDay = birthday.split("-")[2].toInt(),
    )

private val AccountInfoResponse.parsedBirthday: Triple<Int, Int, Int>
    get() {
        val segments = birthday.split("-")
        val year = segments.getOrNull(0)?.toIntOrNull()
        val month = segments.getOrNull(1)?.toIntOrNull()
        val day = segments.getOrNull(2)?.toIntOrNull()

        return if (year != null && month != null && day != null) {
            Triple(year, month, day)
        } else {
            Triple(0, 0, 0)
        }
    }
