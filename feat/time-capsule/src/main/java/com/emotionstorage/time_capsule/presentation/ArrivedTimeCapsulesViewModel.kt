package com.emotionstorage.time_capsule.presentation

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedArrivedTimeCapsulesUseCase
import com.emotionstorage.presentation.BaseViewModel
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.annotation.OrbitExperimental
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ArrivedTimeCapsulesViewModel @Inject constructor(
    getArrivedTimeCapsules: GetPagedArrivedTimeCapsulesUseCase,
) : BaseViewModel<Unit>(Unit) {
    val arrivedTimeCapsules: Flow<PagingData<TimeCapsuleItemState>> =
        getArrivedTimeCapsules()
            .map { pagingData ->
                pagingData.map {
                    TimeCapsuleMapper.toUi(it)
                }
            }.cachedIn(baseViewModelScope)
}
