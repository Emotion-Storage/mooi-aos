package com.emotionstorage.time_capsule.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.useCase.timeCapsule.GetArrivedTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.emotionstorage.ui.R
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class ArrivedTimeCapsulesState(
    val page: Int = 1,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
)

sealed class ArrivedTimeCapsulesAction {
    object Init : ArrivedTimeCapsulesAction()

    object LoadMore : ArrivedTimeCapsulesAction()

    data class ToggleFavorite(
        val id: Long,
    ) : ArrivedTimeCapsulesAction()
}

sealed class ArrivedTimeCapsulesSideEffect {
    data class ShowToast(
        @StringRes val stringResId: Int,
    ) : ArrivedTimeCapsulesSideEffect()
}

@OptIn(OrbitExperimental::class)
@HiltViewModel
class ArrivedTimeCapsulesViewModel @Inject constructor(
    private val getArrivedTimeCapsules: GetArrivedTimeCapsulesUseCase,
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<ArrivedTimeCapsulesState, ArrivedTimeCapsulesSideEffect> {
    override val container =
        container<ArrivedTimeCapsulesState, ArrivedTimeCapsulesSideEffect>(
            ArrivedTimeCapsulesState(),
        )

    fun onAction(action: ArrivedTimeCapsulesAction) {
        when (action) {
            is ArrivedTimeCapsulesAction.Init -> {
                handleInit()
            }

            is ArrivedTimeCapsulesAction.LoadMore -> {
                handleLoadMore()
            }

            is ArrivedTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handleInit() =
        intent {
            handleGetArrivedTimeCapsules(isInit = true)
        }

    private fun handleLoadMore() =
        intent {
            handleGetArrivedTimeCapsules(isInit = false)
        }

    private suspend fun handleGetArrivedTimeCapsules(isInit: Boolean) =
        subIntent {
            val page: Int = if (isInit) 1 else state.page + 1

            collectDataState(
                flow = getArrivedTimeCapsules(page = page),
                onSuccess = { timeCapsules ->
                    reduce {
                        state.copy(
                            page = page,
                            timeCapsules =
                                (if (isInit) emptyList() else state.timeCapsules) +
                                    timeCapsules.map { TimeCapsuleMapper.toUi(it) },
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("Failed to get arrived time capsules, $throwable")
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
                        if (it == FavoriteResult.ADDED) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_added))
                            updateFavorite(id, true)
                        } else if (it == FavoriteResult.REMOVED) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_removed))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == FavoriteResult.FULL) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_full))
                        }
                    },
                )
            }
        }
}
