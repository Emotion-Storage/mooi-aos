package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.useCase.myPage.GetAccountInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AccountInfoState())
    val state: StateFlow<AccountInfoState> = _state

    init {
        viewModelScope.launch {
            getAccountInfoUseCase().handle(
                onSuccess = { data ->
                    _state.value =
                        _state.value.copy(
                            email = data.email,
                            authProvider = AuthProvider.valueOf(data.socialType.uppercase()),
                            gender =
                                when (data.gender.uppercase()) {
                                    "MALE" -> "남성"
                                    "FEMALE" -> "여성"
                                    else -> "기타"
                                },
                            birthYear = data.birthYear,
                            birthMonth = data.birthMonth,
                            birthDay = data.birthDay,
                        )
                },
                onError = { throwable, _ ->
                    // 에러 처리 필요
                },
                onLoading = { isLoading ->
                    // do Nothing
                },
            )
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
