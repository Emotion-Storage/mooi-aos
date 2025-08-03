package com.emotionstorage.tutorial.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.common.DataResource
import com.emotionstorage.tutorial.domain.NicknameState
import com.emotionstorage.tutorial.domain.ValidateNicknameUseCase
import com.emotionstorage.tutorial.presentation.InputNicknameViewModel.State.InputState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InputNicknameEvent {
    fun onNicknameChange(nickname: String)
}

// todo: fix state not updating in ui screen!!!!!

@HiltViewModel
class InputNicknameViewModel @Inject constructor(
    private val validateNickname: ValidateNicknameUseCase
) : ViewModel(), InputNicknameEvent {
    private val _nickname = MutableStateFlow("")
    private val _nicknameInputState = MutableStateFlow(InputState.EMPTY)
    private val _nicknameHelperMessage = MutableStateFlow<String?>(null)
    private val _state = MutableStateFlow(State())

    val state: StateFlow<State> = _state.asStateFlow()
    val event: InputNicknameEvent = this@InputNicknameViewModel

    init {
        Log.d("InputNicknameViewModel", "InputNicknameViewModel created!")

        viewModelScope.launch {
            combine(
                _nickname,
                _nicknameInputState,
                _nicknameHelperMessage
            ) { nickname, nicknameInputState, nicknameHelperMessage ->
                State(nickname, nicknameInputState, nicknameHelperMessage)
            }.collect {
                Log.d("InputNicknameViewModel", "State updated: $it")
                _state.update { it }
            }
        }
    }

    override fun onNicknameChange(nickname: String) {
        viewModelScope.launch {
            Log.d("InputNicknameViewModel", "onNicknameChange: $nickname")

            // update nickname
            _nickname.update { nickname }

            // update nickname input state & helper message
            val result = validateNickname(nickname)
            if (result is DataResource.Success) {
                _nicknameInputState.update {
                    when (result.data) {
                        NicknameState.INVALID_EMPTY -> InputState.EMPTY
                        NicknameState.INVALID_CHAR -> InputState.INVALID
                        NicknameState.INVALID_LENGTH -> InputState.INVALID
                        NicknameState.VALID -> InputState.VALID
                        else -> InputState.EMPTY
                    }
                }
                _nicknameHelperMessage.update {
                    result.data.message
                }
            }
        }
    }

    data class State(
        val nickname: String = "",
        val nicknameInputState: InputState = InputState.EMPTY,
        val nicknameHelperMessage: String? = null
    ) {
        enum class InputState {
            EMPTY, INVALID, VALID
        }
    }
}

