package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.domain.useCase.chat.MarkChatIntroSeenUseCase
import com.emotionstorage.domain.useCase.chat.ObserveChatIntroSeenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AiChatIntroViewModel @Inject constructor(
    observesIntroSeenUseCase: ObserveChatIntroSeenUseCase,
    private val markIntroSeenUseCase: MarkChatIntroSeenUseCase,
) : ViewModel() {
    val introSeen =
        observesIntroSeenUseCase()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1_000),
                false,
            )

    fun onIntroSeenChanged(value: Boolean) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                markIntroSeenUseCase(value)
            }
        }
}
