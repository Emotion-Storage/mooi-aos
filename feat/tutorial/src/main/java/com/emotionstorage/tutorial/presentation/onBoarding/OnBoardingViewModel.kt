package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.model.SignupForm.GENDER
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.auth.domain.usecase.LoginWithIdTokenUseCase
import com.emotionstorage.auth.domain.usecase.SignupUseCase
import com.emotionstorage.auth.presentation.LoginViewModel.State
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User.AuthProvider
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

interface OnBoardingEvent {
    fun onProviderIdTokenReceived(provider: AuthProvider, idToken: String)
    fun onNicknameInputComplete(nickname: String)
    fun onGenderBirthInputComplete(gender: GENDER, birth: LocalDate)
    fun onExpectationsSelectComplete(expectations: List<String>)
    fun onAgreeTermsInputComplete(
        isTermAgreed: Boolean,
        isPrivacyAgreed: Boolean,
        isMarketingAgreed: Boolean
    )

    suspend fun onSignup()
    suspend fun onLogin()
}

/**
 * Shared viewmodel
 * - used to share SignupForm between onboarding screens
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val signup: SignupUseCase,
    private val loginWithIdToken: LoginWithIdTokenUseCase
) : ViewModel(), OnBoardingEvent {
    private val _signupForm = MutableStateFlow(SignupForm())
    private val _signupState = MutableStateFlow(State.AuthState.IDLE)
    private val _loginState = MutableStateFlow(State.AuthState.IDLE)

    val state = combine(
        _signupForm,
        _signupState,
        _loginState
    ) { signupForm, signupState, loginState ->
        State(signupForm, signupState, loginState)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: OnBoardingEvent = this

    override fun onProviderIdTokenReceived(provider: AuthProvider, idToken: String) {
        _signupForm.update { it.copy(provider = provider, idToken = idToken) }
    }

    override fun onNicknameInputComplete(nickname: String) {
        _signupForm.update { it.copy(nickname = nickname) }
    }

    override fun onGenderBirthInputComplete(gender: GENDER, birth: LocalDate) {
        _signupForm.update { it.copy(gender = gender, birthday = birth) }
    }

    override fun onExpectationsSelectComplete(expectations: List<String>) {
        _signupForm.update { it.copy(expectations = expectations) }
    }

    override fun onAgreeTermsInputComplete(
        isTermAgreed: Boolean,
        isPrivacyAgreed: Boolean,
        isMarketingAgreed: Boolean
    ) {
        _signupForm.update {
            it.copy(
                isTermAgreed = isTermAgreed,
                isPrivacyAgreed = isPrivacyAgreed,
                isMarketingAgreed = isMarketingAgreed
            )
        }
    }

    override suspend fun onSignup() {
        if (_signupForm.value.provider == null) {
            Logger.e("provider is null")
            _signupState.update { State.AuthState.ERROR }
            return
        }
        if (_signupForm.value.idToken == null) {
            Logger.e("idToken is null")
            _signupState.update { State.AuthState.ERROR }
            return
        }

        viewModelScope.launch {
            signup(_signupForm.value).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _signupState.update { if (result.isLoading) State.AuthState.LOADING else State.AuthState.IDLE }
                    }

                    is DataState.Success -> {
                        Logger.i(result.toString())
                        _signupState.update { State.AuthState.SUCCESS }
                    }

                    is DataState.Error -> {
                        Logger.e(result.toString())
                        _signupState.update { State.AuthState.ERROR }
                    }
                }
            }
        }
    }

    override suspend fun onLogin() {
        if (_signupForm.value.provider == null) {
            Logger.e("provider is null")
            _loginState.update { State.AuthState.ERROR }
            return
        }
        if (_signupForm.value.idToken == null) {
            Logger.e("idToken is null")
            _loginState.update { State.AuthState.ERROR }
            return
        }

        viewModelScope.launch {
            loginWithIdToken(_signupForm.value.provider!!, _signupForm.value.idToken!!).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _loginState.update { if (result.isLoading) State.AuthState.LOADING else State.AuthState.IDLE }
                    }

                    is DataState.Success -> {
                        Logger.i(result.toString())
                        _loginState.update { State.AuthState.SUCCESS }
                    }

                    is DataState.Error -> {
                        Logger.e(result.toString())
                        _loginState.update { State.AuthState.ERROR }
                    }
                }
            }

        }
    }

    data class State(
        val signupForm: SignupForm = SignupForm(),
        val signupState: AuthState = AuthState.IDLE,
        val loginState: AuthState = AuthState.IDLE
    ) {
        enum class AuthState {
            IDLE,
            LOADING,
            SUCCESS,
            ERROR
        }
    }
}