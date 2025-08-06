package com.emotionstorage.tutorial.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.LoginWithIdTokenUseCase
import com.emotionstorage.domain.model.User.AuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

interface SignupCompleteEvent {
    suspend fun onLogin(provider: AuthProvider, idToken: String): Boolean
}

@HiltViewModel
class SignupCompleteViewModel @Inject constructor(
    private val loginWithIdTokenUseCase: LoginWithIdTokenUseCase,
) : ViewModel(), SignupCompleteEvent {

    val event: SignupCompleteEvent = this@SignupCompleteViewModel

    override suspend fun onLogin(
        provider: AuthProvider,
        idToken: String
    ): Boolean {
        return viewModelScope.async {
            try {
                return@async loginWithIdTokenUseCase(provider = provider, idToken = idToken)
            } catch (e: Exception) {
                e.printStackTrace()
                return@async false
            }
        }.await()
    }
}