package com.emotionstorage.ai_chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emotionstorage.ai_chat.domain.repo.AiChatIntroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiChatIntroViewModel @Inject constructor(
    private val repository: AiChatIntroRepository,
) : ViewModel() {
    val introSeen =
        repository
            .introSeen
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1_000),
                false,
            )

    fun setIntroSeen(value: Boolean) =
        viewModelScope.launch {
            repository.setIntroSeen(value)
        }
}
