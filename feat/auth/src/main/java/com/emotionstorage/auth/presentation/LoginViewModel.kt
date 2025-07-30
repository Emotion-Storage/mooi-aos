package com.emotionstorage.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.User.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

interface LoginScreenEvent {
    fun onLoginButtonClick(
        provider: AuthProvider,
        navToHome: () -> Unit = {},
        navToOnBoarding: () -> Unit = {}
    )
}

@HiltViewModel
class LoginViewModel
@Inject constructor() : ViewModel(), LoginScreenEvent {
    val event: LoginScreenEvent = this@LoginViewModel

    init {
        Log.d("LoginViewModel", "LoginViewModel init")
    }

    override fun onLoginButtonClick(
        provider: AuthProvider,
        navToHome: () -> Unit,
        navToOnBoarding: () -> Unit
    ) {
        // todo: call login use case
        Log.d("LoginViewModel", "onLoginButtonClick: $provider")
    }
}