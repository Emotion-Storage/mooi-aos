package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface AgreeTermsEvent {
    fun onToggleAllAgree()
    fun onToggleTermAgree()
    fun onTogglePrivacyAgree()
    fun onToggleMarketingAgree()
    fun onToggleAgeAgree()
}


@HiltViewModel
class AgreeTermsViewModel @Inject constructor() : ViewModel(), AgreeTermsEvent {
    private val _isAllAgree = MutableStateFlow(false)
    private val _isTermAgree = MutableStateFlow(false)
    private val _isPrivacyAgree = MutableStateFlow(false)
    private val _isMarketingAgree = MutableStateFlow(false)
    private val _isAgeAgree = MutableStateFlow(false)

    val state = combine(
        _isAllAgree,
        _isTermAgree,
        _isPrivacyAgree,
        _isMarketingAgree,
        _isAgeAgree
    ) { isAllAgree, isTermAgree, isPrivacyAgree, isMarketingAgree, isAgeAgree ->
        State(
            isAllAgree = isAllAgree,
            isTermAgree = isTermAgree,
            isPrivacyAgree = isPrivacyAgree,
            isMarketingAgree = isMarketingAgree,
            isAgeAgree = isAgeAgree
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: AgreeTermsEvent = this@AgreeTermsViewModel

    init {
        viewModelScope.launch {
            _isAllAgree.collect { isAllAgree ->
                if (isAllAgree) {
                    _isTermAgree.update { true }
                    _isPrivacyAgree.update { true }
                    _isMarketingAgree.update { true }
                    _isAgeAgree.update { true }
                }
            }
        }

        viewModelScope.launch {
            combine(
                _isTermAgree,
                _isPrivacyAgree,
                _isMarketingAgree,
                _isAgeAgree
            ) { isTermAgree, isPrivacyAgree, isMarketingAgree, isAgeAgree ->
                isTermAgree && isPrivacyAgree && isMarketingAgree && isAgeAgree
            }.collect { isAllAgreed ->
                if (isAllAgreed) {
                    _isAllAgree.update { true }
                } else {
                    _isAllAgree.update { false }
                }
            }
        }
    }

    override fun onToggleAllAgree() {
        _isAllAgree.update { !it }
    }

    override fun onToggleTermAgree() {
        _isTermAgree.update { !it }
    }

    override fun onTogglePrivacyAgree() {
        _isPrivacyAgree.update { !it }
    }

    override fun onToggleMarketingAgree() {
        _isMarketingAgree.update { !it }
    }

    override fun onToggleAgeAgree() {
        _isAgeAgree.update { !it }
    }

    data class State(
        val isAllAgree: Boolean = false,
        val isTermAgree: Boolean = false,
        val isPrivacyAgree: Boolean = false,
        val isMarketingAgree: Boolean = false,
        val isAgeAgree: Boolean = false,
    ) {
        val isSignupCompleteButtonEnabled: Boolean
            get() = isTermAgree && isPrivacyAgree && isAgeAgree
    }
}