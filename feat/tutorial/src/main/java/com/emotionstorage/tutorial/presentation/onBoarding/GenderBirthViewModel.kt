package com.emotionstorage.tutorial.presentation.onBoarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.model.SignupForm.GENDER
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthViewModel.State.PickerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val MIN_AGE = 14
private const val MIN_YEAR = 1970

interface GenderBirthEvent {
    fun onGenderSelect(gender: GENDER?)

    fun onYearPickerSelect(year: String)

    fun onMonthPickerSelect(month: String)

    fun onDayPickerSelect(day: String)
}

@HiltViewModel
class GenderBirthViewModel
    @Inject
    constructor() :
    ViewModel(),
        GenderBirthEvent {
        private val pGender = MutableStateFlow<GENDER?>(null)
        private val pBirthYear = MutableStateFlow<String?>(null)
        private val pBirthMonth = MutableStateFlow<String?>(null)
        private val pBirthDay = MutableStateFlow<String?>(null)
        private val pBirthDayRange: MutableStateFlow<IntRange> = MutableStateFlow(1..31)

        val state =
            combine(
                pGender,
                pBirthYear,
                pBirthMonth,
                pBirthDay,
                pBirthDayRange,
            ) { gender, birthYear, birthMonth, birthDay, birthDayRange ->
                State(
                    gender = gender,
                    yearPickerState =
                        PickerState(
                            selectedValue = birthYear,
                            range = (MIN_YEAR..LocalDate.now().year).toList().map { it.toString() },
                            enabled = true,
                        ),
                    monthPickerState =
                        PickerState(
                            selectedValue = birthMonth,
                            range = (1..12).toList().map { it.toString() },
                            enabled = birthYear != null,
                        ),
                    dayPickerState =
                        PickerState(
                            selectedValue = birthDay,
                            range = birthDayRange.toList().map { it.toString() },
                            enabled = birthYear != null && birthMonth != null,
                        ),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = State(),
            )
        val event: GenderBirthEvent = this@GenderBirthViewModel

        init {
            viewModelScope.launch {
                combine(pBirthYear, pBirthMonth) { year, month ->
                    year to month
                }.collect { (year, month) ->
                    pBirthDayRange.update {
                        if (year == null || month == null) {
                            1..31
                        } else {
                            1..LocalDate.of(year.toInt(), month.toInt(), 1).lengthOfMonth()
                        }
                    }
                }
            }
        }

        override fun onGenderSelect(gender: GENDER?) {
            pGender.update { gender }
        }

        override fun onYearPickerSelect(year: String) {
            pBirthYear.update { year }
            pBirthMonth.update { null }
            pBirthDay.update { null }
        }

        override fun onMonthPickerSelect(month: String) {
            pBirthMonth.update { month }
            pBirthDay.update { null }
        }

        override fun onDayPickerSelect(day: String) {
            pBirthDay.update { day }
        }

        data class State(
            val gender: GENDER? = null,
            val yearPickerState: PickerState =
                PickerState(
                    range = (LocalDate.now().year - MIN_AGE..MIN_YEAR).toList().map { it.toString() },
                    enabled = true,
                ),
            val monthPickerState: PickerState =
                PickerState(
                    range = (1..12).toList().map { it.toString().format("%2d") },
                    enabled = true,
                ),
            val dayPickerState: PickerState =
                PickerState(
                    range = (1..31).toList().map { it.toString().format("%2d") },
                    enabled = true,
                ),
        ) {
            val isNextButtonEnabled: Boolean
                get() =
                    gender != null &&
                        yearPickerState.selectedValue != null &&
                        monthPickerState.selectedValue != null &&
                        dayPickerState.selectedValue != null

            data class PickerState(
                val selectedValue: String? = null,
                val range: List<String> = emptyList(),
                val enabled: Boolean = true,
            )
        }
    }
