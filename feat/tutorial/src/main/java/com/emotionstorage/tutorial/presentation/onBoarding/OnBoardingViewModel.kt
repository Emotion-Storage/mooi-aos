package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.model.SignupForm.GENDER
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

interface OnBoardingEvent {
    fun onIdTokenReceived(idToken: String)
    fun onNicknameInputComplete(nickname: String)
    fun onGenderSelectComplete(gender: GENDER)
    fun onBirthdaySelectComplete(birthday: LocalDate)
    fun onExpectationsSelectComplete(expectations: List<String>)
    // todo: add agree terms input complete events
}

/**
 * Shared viewmodel
 * - used to share SignupForm between onboarding screens
 */
@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel(), OnBoardingEvent {
    private val _signupForm = MutableStateFlow(SignupForm())
    val signupForm: StateFlow<SignupForm> = _signupForm.asStateFlow()

    override fun onIdTokenReceived(idToken: String) {
        _signupForm.update { it.copy(idToken = idToken) }
    }

    override fun onNicknameInputComplete(nickname: String) {
        _signupForm.update { it.copy(nickname = nickname) }
    }

    override fun onGenderSelectComplete(gender: GENDER) {
        _signupForm.update { it.copy(gender = gender) }
    }

    override fun onBirthdaySelectComplete(birthday: LocalDate) {
        _signupForm.update { it.copy(birthday = birthday) }
    }

    override fun onExpectationsSelectComplete(expectations: List<String>) {
        _signupForm.update { it.copy(expectations = expectations) }
    }
}