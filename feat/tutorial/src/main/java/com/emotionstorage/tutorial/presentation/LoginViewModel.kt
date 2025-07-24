package com.emotionstorage.tutorial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    init {
        Log.d("LoginViewModel", "LoginViewModel initialized")
    }
}
