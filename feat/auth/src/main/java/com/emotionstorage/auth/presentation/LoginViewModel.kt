package com.emotionstorage.auth.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.LoginUseCase
import com.emotionstorage.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

interface LoginEvent {
    suspend fun onLoginButtonClick(
        provider: User.AuthProvider,
        onSuccess: () -> Unit = {},
        onError: () -> Unit = {}
    )
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val login: LoginUseCase
) : ViewModel(), LoginEvent {

    val event = this@LoginViewModel

    init {
        Log.d("LoginViewModel", "LoginViewModel initialized")
    }

    override suspend fun onLoginButtonClick(
        provider: User.AuthProvider,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            Log.d("LoginViewModel", "onLoginButtonClick called with provider: $provider")
            if (login(provider)) onSuccess()
            else onError()
        }
    }
}
