package com.emotionstorage.tutorial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// todo: share viewmodel within on boarding screens
@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {
    init {
        Log.d("OnBoardingViewModel", "OnBoardingViewModel init")
    }
}