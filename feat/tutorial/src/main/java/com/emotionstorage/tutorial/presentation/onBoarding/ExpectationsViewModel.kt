package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.auth.domain.model.Expectation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

interface ExpectationsEvent {
    fun onToggleExpectation(index: Int)
}

@HiltViewModel
class ExpectationsViewModel
    @Inject
    constructor() : ViewModel(), ExpectationsEvent {
        private val pSelectedExpectations = MutableStateFlow(emptyList<Expectation>())

        val state =
            combine(pSelectedExpectations) { (selectedExpectations) ->
                State(selectedExpectations = selectedExpectations)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = State(),
            )
        val event: ExpectationsEvent = this@ExpectationsViewModel

        override fun onToggleExpectation(index: Int) {
            val expectation = state.value.expectations[index]

            if (pSelectedExpectations.value.contains(expectation)) {
                pSelectedExpectations.update { expectations ->
                    expectations.filter { it != expectation }
                }
            } else {
                pSelectedExpectations.update { expectations ->
                    expectations + expectation
                }
            }
        }

        data class State(
            val expectations: List<Expectation> = Expectation.values().toList(),
            val selectedExpectations: List<Expectation> = emptyList(),
        ) {
            val isNextButtonEnabled: Boolean
                get() = selectedExpectations.isNotEmpty()
        }
    }
