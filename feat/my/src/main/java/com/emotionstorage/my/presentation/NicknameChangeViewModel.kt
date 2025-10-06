package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.user.NicknameState
import com.emotionstorage.domain.useCase.user.UpdateUserNicknameUseCase
import com.emotionstorage.domain.useCase.user.ValidateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InputNicknameEvent {
    fun onNicknameChange(nickname: String)
}

@HiltViewModel
class NicknameChangeViewModel @Inject constructor(
    private val validateNicknameUseCase: ValidateNicknameUseCase,
    private val updateNicknameUseCase: UpdateUserNicknameUseCase,
) : ViewModel(),
    InputNicknameEvent {
    data class State(
        val nickname: String = "",
        val inputState: InputState = InputState.EMPTY,
        val helperMessage: String? = null,
        val submitting: Boolean = false,
        val submitError: String? = null,
    ) {
        enum class InputState { EMPTY, INVALID, VALID }

        val buttonEnabled: Boolean get() = inputState == InputState.VALID && !submitting
    }

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    override fun onNicknameChange(input: String) {
        if (input.length > 8) return
        _state.update { it.copy(nickname = input) }
    }

    init {
        viewModelScope.launch {
            state
                .map { it.nickname }
                .distinctUntilChanged()
                .debounce(120)
                .mapLatest { nickname ->
                    validateNicknameUseCase(nickname)
                }.collect { result ->
                    if (result is DataState.Success) {
                        val (st, msg) =
                            when (result.data) {
                                NicknameState.INVALID_EMPTY -> State.InputState.EMPTY to result.data.message
                                NicknameState.INVALID_CHAR -> State.InputState.INVALID to result.data.message
                                NicknameState.INVALID_LENGTH -> State.InputState.INVALID to result.data.message
                                NicknameState.VALID -> State.InputState.VALID to result.data.message
                            }
                        _state.update { it.copy(inputState = st, helperMessage = msg) }
                    } else if (result is DataState.Error) {
                        _state.update {
                            it.copy(inputState = State.InputState.INVALID, helperMessage = result.data.toString())
                        }
                    }
                }
        }
    }

    fun submit(onSuccess: (String) -> Unit = {}) {
        val state = _state.value
        if (!state.buttonEnabled) return
        viewModelScope.launch {
            _state.update { it.copy(submitting = true, submitError = null) }
            when (updateNicknameUseCase(state.nickname)) {
                is DataState.Success -> {
                    _state.update { it.copy(submitting = false) }
                    onSuccess(state.nickname)
                }

                is DataState.Error -> {
                    _state.update {
                        it.copy(
                            submitting = false,
                            submitError = null,
                            inputState = State.InputState.INVALID,
                            helperMessage = null,
                        )
                    }
                }

                else -> _state.update { it.copy(submitting = false) }
            }
        }
    }
}
