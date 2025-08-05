package com.emotionstorage.tutorial.presentation.onBoarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.common.DataResource
import com.emotionstorage.tutorial.domain.NicknameState
import com.emotionstorage.tutorial.domain.ValidateNicknameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

interface InputNicknameEvent {
    fun onNicknameChange(nickname: String)
}

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val validateNicknameUseCase: ValidateNicknameUseCase
) : ViewModel(), InputNicknameEvent {
    private val _nickname = MutableStateFlow("")
    private val _nicknameInputState = MutableStateFlow(State.InputState.EMPTY)
    private val _nicknameHelperMessage = MutableStateFlow<String?>(null)

    val state = combine(
        _nickname,
        _nicknameInputState,
        _nicknameHelperMessage
    ) { nickname, nicknameInputState, nicknameHelperMessage ->
        State(nickname, nicknameInputState, nicknameHelperMessage)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = State()
    )
    val event: InputNicknameEvent = this@NicknameViewModel

    init {
        Log.d("InputNicknameViewModel", "InputNicknameViewModel created!")

        viewModelScope.launch {
            _nickname.collect {
                validateNickname(it)
            }
        }
    }

    override fun onNicknameChange(nickname: String) {
        if (nickname.length > 8) return
        _nickname.update { nickname }
    }

    private fun validateNickname(nickname: String) {
        val result = validateNicknameUseCase(nickname)

        if (result is DataResource.Success) {
            _nicknameInputState.update {
                when (result.data) {
                    NicknameState.INVALID_EMPTY -> State.InputState.EMPTY
                    NicknameState.INVALID_CHAR -> State.InputState.INVALID
                    NicknameState.INVALID_LENGTH -> State.InputState.INVALID
                    NicknameState.VALID -> State.InputState.VALID
                    else -> State.InputState.EMPTY
                }
            }
            _nicknameHelperMessage.update {
                result.data.message
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

