package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.myPage.GetAccountInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AccountInfoState())
    val state: StateFlow<AccountInfoState> = _state

    init {
        viewModelScope.launch {
            getAccountInfoUseCase().collect { dataState ->
                if (dataState is DataState.Success) {
                    val info = dataState.data
                    _state.value = AccountInfoState(
                        email = info.email,
                        authProvider = AuthProvider.valueOf(info.socialType.uppercase()),
                        gender = when (info.gender.uppercase()) {
                            "MALE" -> "남성"
                            else -> "여성"
                        },
                        birthYear = info.birthYear,
                        birthMonth = info.birthMonth,
                        birthDay = info.birthDay
                    )
                }
            }
        }
    }
}

data class AccountInfoState(
    val email: String = "",
    val authProvider: AuthProvider = AuthProvider.KAKAO,
    val gender: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0,
)

enum class AuthProvider { GOOGLE, KAKAO }


