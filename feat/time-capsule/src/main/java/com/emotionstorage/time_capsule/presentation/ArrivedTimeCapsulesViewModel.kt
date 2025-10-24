package com.emotionstorage.time_capsule.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.SetFavoriteResult
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
    val pageNum: Int = 1,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
)

sealed class ArrivedTimeCapsulesAction {
    object Init : ArrivedTimeCapsulesAction()

    object LoadMore : ArrivedTimeCapsulesAction()


    data class ToggleFavorite(
        val id: String,
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

    private fun handleInit() = intent {
        handleGetArrivedTimeCapsules()
    }

    private fun handleLoadMore() = intent {
        handleGetArrivedTimeCapsules(state.pageNum)
    }


    private suspend fun handleGetArrivedTimeCapsules(page: Int = 0) = subIntent {
        collectDataState(
            flow = getArrivedTimeCapsules(page = page + 1),
            onSuccess = { timeCapsules ->
                reduce {
                    state.copy(
                        pageNum = page + 1,
                        timeCapsules = timeCapsules.map { TimeCapsuleMapper.toUi(it) },
                    )
                }
            },
            onError = { throwable, _ ->
                Logger.e("Failed to get arrived time capsules, $throwable")
            },
        )
    }

    private fun handleToggleFavorite(id: String) =
        intent {
            if (state.timeCapsules.find { it.id == id } == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }

            val newIsFavorite = !state.timeCapsules.find { it.id == id }!!.isFavorite

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
                    flow = setFavorite(id, newIsFavorite),
                    onSuccess = {
                        if (it == SetFavoriteResult.ADDED) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_added))
                            updateFavorite(id, true)
                        } else if (it == SetFavoriteResult.REMOVED) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_removed))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == SetFavoriteResult.FULL) {
                            postSideEffect(ShowToast(stringResId = R.string.toast_favorite_full))
                        }
                    },
                )
            }
        }
}
