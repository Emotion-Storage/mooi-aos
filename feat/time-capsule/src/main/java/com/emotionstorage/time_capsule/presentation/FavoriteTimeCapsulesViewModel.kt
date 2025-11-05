package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedFavoriteTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowFavoriteToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: FavoriteSortBy = FavoriteSortBy.NEWEST,
    val timeCapsulesFlow: Flow<PagingData<TimeCapsuleItemState>>? = null,
)

sealed class FavoriteTimeCapsulesAction {
    object Init : FavoriteTimeCapsulesAction()

    data class SetSortOrder(
        val sortOrderLabel: String,
    ) : FavoriteTimeCapsulesAction()

    data class ToggleFavorite(
        val id: Long,
        val prevIsFavorite: Boolean,
    ) : FavoriteTimeCapsulesAction()
}

sealed class FavoriteTimeCapsulesSideEffect {
    data class ShowFavoriteToast(
        val favoriteResult: FavoriteResult,
    ) : FavoriteTimeCapsulesSideEffect()
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    private val getFavoriteTimeCapsules: GetPagedFavoriteTimeCapsulesUseCase,
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect> {
    override val container =
        container<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect>(
            FavoriteTimeCapsulesState(),
        )

    fun onAction(action: FavoriteTimeCapsulesAction) {
        when (action) {
            is FavoriteTimeCapsulesAction.Init -> {
                handleInit()
            }

            is FavoriteTimeCapsulesAction.SetSortOrder -> {
                handleSetSortOrder(action.sortOrderLabel)
            }

            is FavoriteTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id, action.prevIsFavorite)
            }
        }
    }

    private fun handleInit() = setSortOrder(FavoriteSortBy.NEWEST)

    private fun handleSetSortOrder(sortOrderLabel: String) =
        setSortOrder(FavoriteSortBy.getByLabel(sortOrderLabel))


    private fun setSortOrder(sortOrder: FavoriteSortBy) = intent {
        try {
            reduce {
                state.copy(
                    sortOrder = sortOrder,
                    timeCapsulesFlow = getFavoriteTimeCapsules(sortOrder).map { pagingData ->
                        pagingData.map {
                            TimeCapsuleMapper.toUi(it)
                        }
                    }
                )
            }
        } catch (e: Exception) {
            Logger.e("Failed to get favorite time capsules, $e")
        }
    }

    private fun handleToggleFavorite(id: Long, prevIsFavorite: Boolean) =
        intent {
            coroutineScope {
                collectDataState(
                    flow = setFavorite(id, !prevIsFavorite),
                    onSuccess = {
                        postSideEffect(ShowFavoriteToast(it))
                    },
                    onError = { throwable, data ->
                        Logger.e("Failed to toggle favorite, $throwable")
                    },
                )
            }
        }
}
