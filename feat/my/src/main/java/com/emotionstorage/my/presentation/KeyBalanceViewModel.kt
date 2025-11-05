package com.emotionstorage.my.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyBalanceViewModel @Inject constructor(
    private val getKeyCountUseCase: GetKeyCountUseCase
) : ViewModel() {
    private val _keyCountState = MutableStateFlow(KeyCountState())
    val keyCountState: StateFlow<KeyCountState> = _keyCountState

    fun refreshKeyCount() = viewModelScope.launch {
        _keyCountState.update { it.copy(isLoading = true, error = null) }
        getKeyCountUseCase.invoke().handle(
            onSuccess = { count ->
                _keyCountState.update { it.copy(keyCount = count, isLoading = false) }
            },
            onLoading = {
                _keyCountState.update { it.copy(isLoading = true) }
            },
            onError = { throwable, _ ->
                _keyCountState.update { it.copy(error = throwable.message, isLoading = false) }
            }
        )
    }
}

data class KeyCountState(
    val keyCount: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
