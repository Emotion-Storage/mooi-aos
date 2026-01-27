package com.emotionstorage.my.presentation

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.useCase.user.NicknameState
import com.emotionstorage.domain.useCase.user.UpdateUserNicknameUseCase
import com.emotionstorage.domain.useCase.user.ValidateNicknameUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NicknameChangeViewModel @Inject constructor(
    private val validateNickname: ValidateNicknameUseCase,
    private val updateNickname: UpdateUserNicknameUseCase,
) : BaseViewModel<Unit>(Unit),
    InputNicknameEvent {
    data class State(
        val nickname: String = "",
        val inputState: InputState = InputState.EMPTY,
        val helperMessage: String? = null,
        val submitting: Boolean = false,
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
        baseViewModelScope.launch {
            state
                .map { it.nickname }
                .distinctUntilChanged()
                .debounce(120)
                .mapLatest { nickname ->
                    validateNickname(nickname)
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
                            it.copy(inputState = State.InputState.INVALID, helperMessage = "닉네임 검증 중 오류 발생")
                        }
                    }
                }
        }
    }

    fun submit(onSuccess: (String) -> Unit = {}) {
        val state = _state.value
        if (!state.buttonEnabled) return
        baseViewModelScope.launch {
            _state.update { it.copy(submitting = true) }
            when (val result = updateNickname(state.nickname)) {
                is DataState.Success -> {
                    _state.update { it.copy(submitting = false) }
                    onSuccess(state.nickname)
                }

                is DataState.Error -> {
                    _state.update {
                        it.copy(
                            submitting = false,
                            inputState = State.InputState.INVALID,
                            helperMessage = null,
                        )
                    }
                    throw BaseException(
                        message = result.throwable.message ?: "updateNickNameUseCase error",
                        code = result.code,
                        cause = result.throwable,
                    )
                }

                is DataState.Loading -> {
                    // do nothing
                }
            }
        }
    }
}
