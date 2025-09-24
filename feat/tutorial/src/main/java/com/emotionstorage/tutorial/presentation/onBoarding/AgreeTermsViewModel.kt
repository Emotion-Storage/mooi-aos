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
class AgreeTermsViewModel
    @Inject
    constructor() : ViewModel(), AgreeTermsEvent {
        private val pIsAllAgree = MutableStateFlow(false)
        private val pIsTermAgree = MutableStateFlow(false)
        private val pIsPrivacyAgree = MutableStateFlow(false)
        private val pIsMarketingAgree = MutableStateFlow(false)
        private val pIsAgeAgree = MutableStateFlow(false)

        val state =
            combine(
                pIsAllAgree,
                pIsTermAgree,
                pIsPrivacyAgree,
                pIsMarketingAgree,
                pIsAgeAgree,
            ) { isAllAgree, isTermAgree, isPrivacyAgree, isMarketingAgree, isAgeAgree ->
                State(
                    isAllAgree = isAllAgree,
                    isTermAgree = isTermAgree,
                    isPrivacyAgree = isPrivacyAgree,
                    isMarketingAgree = isMarketingAgree,
                    isAgeAgree = isAgeAgree,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = State(),
            )
        val event: AgreeTermsEvent = this@AgreeTermsViewModel

        init {
            viewModelScope.launch {
                pIsAllAgree.collect { isAllAgree ->
                    if (isAllAgree) {
                        pIsTermAgree.update { true }
                        pIsPrivacyAgree.update { true }
                        pIsMarketingAgree.update { true }
                        pIsAgeAgree.update { true }
                    }
                }
            }

            viewModelScope.launch {
                combine(
                    pIsTermAgree,
                    pIsPrivacyAgree,
                    pIsMarketingAgree,
                    pIsAgeAgree,
                ) { isTermAgree, isPrivacyAgree, isMarketingAgree, isAgeAgree ->
                    isTermAgree && isPrivacyAgree && isMarketingAgree && isAgeAgree
                }.collect { isAllAgreed ->
                    if (isAllAgreed) {
                        pIsAllAgree.update { true }
                    } else {
                        pIsAllAgree.update { false }
                    }
                }
            }
        }

        override fun onToggleAllAgree() {
            pIsAllAgree.update { !it }
        }

        override fun onToggleTermAgree() {
            pIsTermAgree.update { !it }
        }

        override fun onTogglePrivacyAgree() {
            pIsPrivacyAgree.update { !it }
        }

        override fun onToggleMarketingAgree() {
            pIsMarketingAgree.update { !it }
        }

        override fun onToggleAgeAgree() {
            pIsAgeAgree.update { !it }
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
