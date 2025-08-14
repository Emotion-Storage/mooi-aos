package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.model.SignupForm.GENDER
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.auth.domain.usecase.SignupUseCase
import com.emotionstorage.domain.model.User.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    suspend fun onSignup(): Boolean
    suspend fun onLogin(): Boolean
}

/**
 * Shared viewmodel
 * - used to share SignupForm between onboarding screens
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    private val signup: SignupUseCase,
    private val login: LoginUseCase
) : ViewModel(), OnBoardingEvent {
    private var _provider: AuthProvider? = null

    private val _signupForm = MutableStateFlow(SignupForm())
    val signupForm: StateFlow<SignupForm> = _signupForm.asStateFlow()

    override fun onProviderIdTokenReceived(provider: AuthProvider, idToken: String) {
        _provider = provider
        _signupForm.update { it.copy(idToken = idToken) }
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

    override suspend fun onSignup(): Boolean {
        val deferredResult = viewModelScope.async {
            try {
                if (_provider == null) throw Exception("Provider is null")
                return@async signup(provider = _provider!!, signupForm = _signupForm.value)
            } catch (e: Exception) {
                e.printStackTrace()
                return@async false
            }
        }
        return deferredResult.await()
    }


    override suspend fun onLogin(): Boolean {
        val deferredResult = viewModelScope.async {
            try {
                if (_provider == null) throw Exception("Provider is null")
                return@async login(provider = _provider!!)
            } catch (e: Exception) {
                e.printStackTrace()
                return@async false
            }
        }
        return deferredResult.await()
    }
}