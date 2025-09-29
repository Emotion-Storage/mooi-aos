package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.ai_chat.domain.usecase.local.MarkAiChatIntroSeenUseCase
import com.emotionstorage.ai_chat.domain.usecase.local.ObserveAiChatIntroSeenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatIntroViewModel @Inject constructor(
    observesIntroSeenUseCase: ObserveAiChatIntroSeenUseCase,
    private val markIntroSeenUseCase: MarkAiChatIntroSeenUseCase,
) : ViewModel() {
    val introSeen =
        observesIntroSeenUseCase()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1_000),
                false,
            )

    fun onIntroSeenChanged(value: Boolean) = viewModelScope.launch {
        markIntroSeenUseCase(value)
    }
}
