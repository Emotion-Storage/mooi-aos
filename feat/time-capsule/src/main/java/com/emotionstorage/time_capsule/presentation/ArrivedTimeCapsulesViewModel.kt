package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedArrivedTimeCapsulesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.ArrivedTimeCapsulesSideEffect.ShowFavoriteToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

data class ArrivedTimeCapsulesState(
    val timeCapsules: Flow<PagingData<TimeCapsuleItemState>>? = null
)

sealed class ArrivedTimeCapsulesAction {
    object Init : ArrivedTimeCapsulesAction()

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
    private val getArrivedTimeCapsules: GetPagedArrivedTimeCapsulesUseCase,
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

            is ArrivedTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id, action.prevIsFavorite)
            }
        }
    }

    private fun handleInit() =
        intent {
            try {
                val timeCapsules: Flow<PagingData<TimeCapsuleItemState>> =
                    getArrivedTimeCapsules().transform { pagingData ->
                        pagingData.map {
                            TimeCapsuleMapper.toUi(it)
                        }
                    }
                reduce { state.copy(timeCapsules = timeCapsules) }
            } catch (e: Exception) {
                Logger.e("Failed to get arrived time capsules, $e")
                // todo: handle error
            }
        }


    private fun handleToggleFavorite(id: Long, prevIsFavorite: Boolean) =
        intent {
            suspend fun updateFavorite(
                id: Long,
                isFavorite: Boolean,
            ) = reduce {
                state.copy(
                    timeCapsules =
                        state.timeCapsules?.transform { pagingData ->
                            pagingData.map {
                                if (it.id == id) {
                                    it.copy(isFavorite = isFavorite)
                                } else {
                                    it
                                }
                            }
                        },
                )
            }

            coroutineScope {
                collectDataState(
                    flow = setFavorite(id, !prevIsFavorite),
                    onSuccess = {
                        when (it) {
                            FavoriteResult.ADDED -> {
                                updateFavorite(id, true)
                            }

                            FavoriteResult.REMOVED -> {
                                updateFavorite(id, false)
                            }

                            FavoriteResult.FULL -> {
                                // do nothing
                            }
                        }
                        postSideEffect(ShowFavoriteToast(it))
                    },
                    onError = { throwable, data ->
                        Logger.e("Failed to toggle favorite, $throwable")
                    },
                )
            }
        }
}
