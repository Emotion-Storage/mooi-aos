package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.useCase.timeCapsule.GetFavoriteTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase.ToggleToastResult
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
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
        val id: String,
    ) : FavoriteTimeCapsulesAction()
}

sealed class FavoriteTimeCapsulesSideEffect {
    data class ShowToast(
        val toast: FavoriteToast,
    ) : FavoriteTimeCapsulesSideEffect() {
        enum class FavoriteToast(
            val message: String,
        ) {
            FAVORITE_ADDED("즐겨찾기가 설정되었습니다."),
            FAVORITE_REMOVED("즐겨찾기가 해제되었습니다."),
            FAVORITE_FULL("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요."),
        }
    }
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    private val getFavoriteTimeCapsules: GetFavoriteTimeCapsulesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
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
                        state.copy(timeCapsules = timeCapsules.map { TimeCapsuleMapper.toUi(it) }
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("Failed to get favorite time capsules, ${throwable}")
                }
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
                            timeCapsules = timeCapsules.map { TimeCapsuleMapper.toUi(it) }
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("Failed to get favorite time capsules, ${throwable}")
                }
            )
        }

    private fun handleToggleFavorite(id: String) =
        intent {
            if (state.timeCapsules.find { it.id == id } == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }

            suspend fun updateFavorite(
                id: String,
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
                    flow = toggleFavorite(id),
                    onSuccess = {
                        if (it == ToggleToastResult.FAVORITE_ADDED) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_ADDED))
                            updateFavorite(id, true)
                        } else if (it == ToggleToastResult.FAVORITE_REMOVED) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_REMOVED))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == ToggleToastResult.FAVORITE_FULL) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_FULL))
                        }
                    },
                )
            }
        }
}
