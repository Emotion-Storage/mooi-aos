package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedArrivedTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesSideEffect.ShowFavoriteToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed class ArrivedTimeCapsulesAction {
    data class ToggleFavorite(
        val id: Long,
        val prevIsFavorite: Boolean,
    ) : ArrivedTimeCapsulesAction()
}

sealed class ArrivedTimeCapsulesSideEffect {
    data class ShowFavoriteToast(
        val favoriteResult: FavoriteResult,
    ) : ArrivedTimeCapsulesSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ArrivedTimeCapsulesViewModel @Inject constructor(
    getArrivedTimeCapsules: GetPagedArrivedTimeCapsulesUseCase,
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<Unit, ArrivedTimeCapsulesSideEffect> {
    override val container =
        container<Unit, ArrivedTimeCapsulesSideEffect>(Unit)

    // paging data flow should not be managed by orbit!
    val arrivedTimeCapsules: Flow<PagingData<TimeCapsuleItemState>> =
        getArrivedTimeCapsules()
            .transform { pagingData ->
                emit(
                    pagingData.map {
                        TimeCapsuleMapper.toUi(it)
                    },
                )
            }.cachedIn(viewModelScope)

    fun onAction(action: ArrivedTimeCapsulesAction) {
        when (action) {
            is ArrivedTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id, action.prevIsFavorite)
            }
        }
    }

    private fun handleToggleFavorite(
        id: Long,
        prevIsFavorite: Boolean,
    ) = intent {
        collectDataState(
            flow = setFavorite(id, !prevIsFavorite),
            onSuccess = {
                // todo: integrate room paging using remote mediator
                // ui will update automatically, on room entity change
                postSideEffect(ShowFavoriteToast(it))
            },
            onError = { throwable, data ->
                Logger.e("Failed to toggle favorite, $throwable")
            },
        )
    }
}
