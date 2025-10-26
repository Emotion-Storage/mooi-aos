package com.emotionstorage.time_capsule.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.SetFavoriteResult
import com.emotionstorage.domain.useCase.timeCapsule.GetFavoriteTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.emotionstorage.ui.R
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: FavoriteSortBy = FavoriteSortBy.NEWEST,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
)

sealed class FavoriteTimeCapsulesAction {
    object Init : FavoriteTimeCapsulesAction()

    data class SetSortOrder(
        val sortOrderLabel: String,
    ) : FavoriteTimeCapsulesAction()

    data class ToggleFavorite(
        val id: Long,
    ) : FavoriteTimeCapsulesAction()
}

sealed class FavoriteTimeCapsulesSideEffect {
    data class ShowToast(
        @StringRes val stringResId: Int,
    ) : FavoriteTimeCapsulesSideEffect()
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    private val getFavoriteTimeCapsules: GetFavoriteTimeCapsulesUseCase,
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
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handleInit() =
        intent {
            collectDataState(
                flow = getFavoriteTimeCapsules(state.sortOrder),
                onSuccess = { timeCapsules ->
                    reduce {
                        state.copy(
                            timeCapsules = timeCapsules.map { TimeCapsuleMapper.toUi(it) },
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("Failed to get favorite time capsules, $throwable")
                },
            )
        }

    private fun handleSetSortOrder(sortOrderLabel: String) =
        intent {
            val sortOrder = FavoriteSortBy.getByLabel(sortOrderLabel)

            collectDataState(
                flow = getFavoriteTimeCapsules(sortOrder),
                onSuccess = { timeCapsules ->
                    reduce {
                        state.copy(
                            sortOrder = sortOrder,
                            timeCapsules = timeCapsules.map { TimeCapsuleMapper.toUi(it) },
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("Failed to get favorite time capsules, $throwable")
                },
            )
        }

    private fun handleToggleFavorite(id: Long) =
        intent {
            if (state.timeCapsules.find { it.id == id } == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }

            val newIsFavorite = !state.timeCapsules.find { it.id == id }!!.isFavorite

            suspend fun updateFavorite(
                id: Long,
                isFavorite: Boolean,
            ) = reduce {
                state.copy(
                    timeCapsules =
                        state.timeCapsules.map {
                            if (it.id == id) {
                                it.copy(isFavorite = isFavorite)
                            } else {
                                it
                            }
                        },
                )
            }

            coroutineScope {
                collectDataState(
                    flow = setFavorite(id, newIsFavorite),
                    onSuccess = {
                        if (it == SetFavoriteResult.ADDED) {
                            postSideEffect(ShowToast(R.string.toast_favorite_added))
                            updateFavorite(id, true)
                        } else if (it == SetFavoriteResult.REMOVED) {
                            postSideEffect(ShowToast(R.string.toast_favorite_removed))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == SetFavoriteResult.FULL) {
                            postSideEffect(ShowToast(R.string.toast_favorite_full))
                        }
                    },
                )
            }
        }
}
