package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    // make use case
) : ViewModel() {

}

data class AccountInfoState(
    val email: String = "",
    val authProvider: AuthProvider = AuthProvider.KAKAO,
    val gender: String = "",
    val birth: String = "",
)

enum class Gender { MALE, FEMALE }
enum class AuthProvider { GOOGLE, KAKAO }

