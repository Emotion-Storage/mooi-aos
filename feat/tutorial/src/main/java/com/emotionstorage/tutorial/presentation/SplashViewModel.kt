package com.emotionstorage.tutorial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.usecase.AutomaticLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val automaticLogin: AutomaticLoginUseCase
): ViewModel() {
    // todo: splash state - loading(for 2s), done
    // todo: login state - loading, success, fail

    init{
        Log.d("SplashViewModel", "SplashViewModel initialized")

        viewModelScope.launch {
            // set splash state loading
            delay(2000)
            // set splash state done
        }

        viewModelScope.launch {
            // todo: try automatic login
        }
    }
}