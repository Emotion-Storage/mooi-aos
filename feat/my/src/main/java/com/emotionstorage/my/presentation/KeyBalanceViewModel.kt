package com.emotionstorage.my.presentation

import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyBalanceViewModel @Inject constructor(
    private val getKeyCount: GetKeyCountUseCase,
) : BaseViewModel<Unit>(Unit) {
    private val _keyCountState = MutableStateFlow(KeyCountState())
    val keyCountState: StateFlow<KeyCountState> = _keyCountState

    fun refreshKeyCount() =
        baseViewModelScope.launch {
            _keyCountState.update { it.copy(isLoading = true) }
            getKeyCount.invoke().handle(
                onSuccess = { count ->
                    _keyCountState.update { it.copy(keyCount = count, isLoading = false) }
                },
                onLoading = {
                    _keyCountState.update { it.copy(isLoading = true) }
                },
                onError = { throwable, code, data ->
                    _keyCountState.update { it.copy(isLoading = false) }
                    throw BaseException(
                        message = throwable.message ?: "getKeyCount Error",
                        code = code,
                        cause = throwable,
                    )
                },
            )
        }

    fun openWhenToUseDialog() {
        _keyCountState.update {
            it.copy(dialog = KeyDescriptionDialog.WhenToUse)
        }
    }

    fun dismissDialog() {
        _keyCountState.update {
            it.copy(dialog = null)
        }
    }
}

data class KeyCountState(
    val keyCount: Int? = null,
    val isLoading: Boolean = false,
    val dialog: KeyDescriptionDialog? = null,
)

sealed interface KeyDescriptionDialog {
    data object WhenToUse : KeyDescriptionDialog
}
