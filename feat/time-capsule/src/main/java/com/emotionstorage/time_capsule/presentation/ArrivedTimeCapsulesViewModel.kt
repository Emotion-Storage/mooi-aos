package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedArrivedTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ArrivedTimeCapsulesViewModel @Inject constructor(
    getArrivedTimeCapsules: GetPagedArrivedTimeCapsulesUseCase,
) : ViewModel() {
    val arrivedTimeCapsules: Flow<PagingData<TimeCapsuleItemState>> =
        getArrivedTimeCapsules()
            .map { pagingData ->
                pagingData.map {
                    TimeCapsuleMapper.toUi(it)
                }
            }.cachedIn(viewModelScope)
}
