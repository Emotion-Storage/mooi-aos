package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.tutorial.presentation.onBoarding.ExpectationsViewModel.State.Expectation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private val EXPECTATION_CONTENT = listOf(
    "내 감정을 정리하고 싶어요",
    "스트레스를 관리하고 싶어요",
    "후회나 힘든 감정을 털어내고 싶어요",
    "좋은 기억을 오래 간직하고 싶어요",
    "내 감정 패턴을 알고 싶어요",
    "그냥 편하게 내 얘기를 남기고 싶어요"
)

interface ExpectationsEvent {
    fun onToggleExpectation(index: Int)
}

@HiltViewModel
class ExpectationsViewModel @Inject constructor() : ViewModel(), ExpectationsEvent {
    private val _expectations = MutableStateFlow(emptyList<Expectation>())

    val state = combine(_expectations) { (expectations) ->
        State(expectations)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: ExpectationsEvent = this@ExpectationsViewModel

    init {
        _expectations.update {
            EXPECTATION_CONTENT.map { it ->
                Expectation(content = it, isSelected = false)
            }
        }
    }

    override fun onToggleExpectation(index: Int) {
        _expectations.update { expectations ->
            expectations.mapIndexed { i, expectation ->
                if (i == index) {
                    expectation.copy(isSelected = !expectation.isSelected)
                } else {
                    expectation
                }
            }
        }
    }

    data class State(
        val expectations: List<Expectation> = emptyList()
    ) {
        val isNextButtonEnabled: Boolean
            get() = expectations.any { it.isSelected }

        data class Expectation(
            val content: String,
            val isSelected: Boolean
        )
    }
}

