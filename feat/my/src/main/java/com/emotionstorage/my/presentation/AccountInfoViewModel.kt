package com.emotionstorage.my.presentation

import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.useCase.myPage.GetAccountInfoUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountInfoState(
    val email: String = "",
    val authProvider: User.AuthProvider = User.AuthProvider.KAKAO,
    val gender: String = "",
    val birthYear: Int = 0,
    val birthMonth: Int = 0,
    val birthDay: Int = 0,
    val isLoading: Boolean = false,
)

@HiltViewModel
class AccountInfoViewModel @Inject constructor(
    private val getAccountInfo: GetAccountInfoUseCase,
) : BaseViewModel<Unit>(Unit) {
    private val _state = MutableStateFlow(AccountInfoState())
    val state: StateFlow<AccountInfoState> = _state

    init {
        baseViewModelScope.launch {
            getAccountInfo().handle(
                onSuccess = { data ->
                    _state.value =
                        _state.value.copy(
                            email = data.email,
                            authProvider = User.AuthProvider.valueOf(data.socialType.uppercase()),
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
                onError = { throwable, code, data ->
                    _state.value =
                        _state.value.copy(
                            isLoading = false,
                        )
                    throw BaseException(
                        message = throwable.message ?: "getAccountInfoUseCase Error",
                        code = code,
                        cause = throwable,
                    )
                },
                onLoading = { isLoading ->
                    // do Nothing
                },
            )
        }
    }
}
