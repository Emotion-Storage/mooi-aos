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
    fun resetAgreedTerms()

    fun onToggleAllAgreed()

    fun onToggleTermAgreed()

    fun onTogglePrivacyAgreed()

    fun onToggleMarketingAgreed()

    fun onToggleAgeAgreed()
}

@HiltViewModel
class AgreeTermsViewModel @Inject constructor() :
    ViewModel(),
    AgreeTermsEvent {
        private val pIsAllAgreed = MutableStateFlow(false)
        private val pIsTermAgreed = MutableStateFlow(false)
        private val pIsPrivacyAgreed = MutableStateFlow(false)
        private val pIsMarketingAgreed = MutableStateFlow(false)
        private val pIsAgeAgreed = MutableStateFlow(false)

        val state =
            combine(
                pIsAllAgreed,
                pIsTermAgreed,
                pIsPrivacyAgreed,
                pIsMarketingAgreed,
                pIsAgeAgreed,
            ) { isAllAgreed, isTermAgreed, isPrivacyAgreed, isMarketingAgreed, isAgeAgreed ->
                State(
                    isAllAgreed = isAllAgreed,
                    isTermAgreed = isTermAgreed,
                    isPrivacyAgreed = isPrivacyAgreed,
                    isMarketingAgreed = isMarketingAgreed,
                    isAgeAgreed = isAgeAgreed,
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = State(),
            )
        val event: AgreeTermsEvent = this@AgreeTermsViewModel

        init {
            viewModelScope.launch {
                combine(
                    pIsTermAgreed,
                    pIsPrivacyAgreed,
                    pIsMarketingAgreed,
                    pIsAgeAgreed,
                ) { isTermAgree, isPrivacyAgree, isMarketingAgree, isAgeAgree ->
                    isTermAgree && isPrivacyAgree && isMarketingAgree && isAgeAgree
                }.collect { isAllAgreed ->
                    if (isAllAgreed) {
                        pIsAllAgreed.update { true }
                    } else {
                        pIsAllAgreed.update { false }
                    }
                }
            }
        }

        override fun resetAgreedTerms() {
            pIsAllAgreed.update { false }
            pIsTermAgreed.update { false }
            pIsPrivacyAgreed.update { false }
            pIsMarketingAgreed.update { false }
            pIsAgeAgreed.update { false }
        }

        override fun onToggleAllAgreed() {
            (!state.value.isAllAgreed).run{
                pIsAllAgreed.update { this }
                // update all terms
                pIsTermAgreed.update { this }
                pIsPrivacyAgreed.update { this }
                pIsMarketingAgreed.update { this }
                pIsAgeAgreed.update { this }
            }

        }

        override fun onToggleTermAgreed() {
            pIsTermAgreed.update { !it }
        }

        override fun onTogglePrivacyAgreed() {
            pIsPrivacyAgreed.update { !it }
        }

        override fun onToggleMarketingAgreed() {
            pIsMarketingAgreed.update { !it }
        }

        override fun onToggleAgeAgreed() {
            pIsAgeAgreed.update { !it }
        }

        data class State(
            val isAllAgreed: Boolean = false,
            val isTermAgreed: Boolean = false,
            val isPrivacyAgreed: Boolean = false,
            val isMarketingAgreed: Boolean = false,
            val isAgeAgreed: Boolean = false,
        ) {
            val isSignupCompleteButtonEnabled: Boolean
                get() = isTermAgreed && isPrivacyAgreed && isAgeAgreed
        }
    }
