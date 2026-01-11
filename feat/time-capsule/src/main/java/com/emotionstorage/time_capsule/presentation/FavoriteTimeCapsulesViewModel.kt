package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedFavoriteTimeCapsulesUseCase
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: FavoriteSortBy = FavoriteSortBy.NEWEST,
)

sealed class FavoriteTimeCapsulesAction {
    object Init : FavoriteTimeCapsulesAction()

    data class SetSortOrder(
        val sortOrderLabel: String,
    ) : FavoriteTimeCapsulesAction()
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    private val getFavoriteTimeCapsules: GetPagedFavoriteTimeCapsulesUseCase,
) : ViewModel(),
    ContainerHost<FavoriteTimeCapsulesState, Unit> {
    override val container =
        container<FavoriteTimeCapsulesState, Unit>(
            FavoriteTimeCapsulesState(),
        )

    // manage paging flow outside of orbit state
    val pagingFlow: Flow<PagingData<TimeCapsuleItemState>> =
        container.stateFlow
            .map { it.sortOrder }
            .distinctUntilChanged()
            .flatMapLatest { sortOrder ->
                getFavoriteTimeCapsules(sortOrder)
                    .map { pagingData ->
                        pagingData.map(TimeCapsuleMapper::toUi)
                    }
            }
            .cachedIn(viewModelScope)

    fun onAction(action: FavoriteTimeCapsulesAction) {
        when (action) {
            is FavoriteTimeCapsulesAction.Init -> {
                handleInit()
            }

            is FavoriteTimeCapsulesAction.SetSortOrder -> {
                handleSetSortOrder(action.sortOrderLabel)
            }
        }
    }

    private fun handleInit() = intent {
        reduce {
            state.copy(
                sortOrder = FavoriteSortBy.NEWEST,
            )
        }
    }

    private fun handleSetSortOrder(sortOrderLabel: String) =
        intent {
            try {
                reduce {
                    state.copy(
                        sortOrder = FavoriteSortBy.getByLabel(sortOrderLabel),
                    )
                }
            } catch (e: Exception) {
                Logger.e("Failed to get favorite time capsules, $e")
            }
        }
}
