package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteFailToast
import com.emotionstorage.time_capsule.presentation.ToggleFavoriteSideEffect.ShowFavoriteSuccessToast
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class ToggleFavoriteState(
    val favoriteIds: List<Long> = emptyList(),
)

sealed class ToggleFavoriteAction {
    data class Init(
        val favoriteIds: List<Long>,
    ) : ToggleFavoriteAction()

    data class OnToggle(
        val id: Long,
    ) : ToggleFavoriteAction()
}

sealed class ToggleFavoriteSideEffect {
    data class ShowFavoriteSuccessToast(
        val isFavorite: Boolean,
    ) : ToggleFavoriteSideEffect()

    object ShowFavoriteFailToast : ToggleFavoriteSideEffect()
}

@HiltViewModel
class ToggleFavoriteViewModel @Inject constructor(
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<ToggleFavoriteState, ToggleFavoriteSideEffect> {
    override val container: Container<ToggleFavoriteState, ToggleFavoriteSideEffect> =
        container(ToggleFavoriteState())

    fun onAction(action: ToggleFavoriteAction) {
        when (action) {
            is ToggleFavoriteAction.Init -> {
                intent {
                    reduce {
                        state.copy(favoriteIds = action.favoriteIds)
                    }
                }
            }

            is ToggleFavoriteAction.OnToggle -> {
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handleToggleFavorite(id: Long) =
        intent {
            setFavorite(id, id !in state.favoriteIds).handle(
                onSuccess = {
                    reduce {
                        state.copy(
                            favoriteIds = if (it) state.favoriteIds + id else state.favoriteIds - id,
                        )
                    }
                    postSideEffect(ShowFavoriteSuccessToast(it))
                },
                onError = { throwable, code, data ->
                    when (code) {
                        ErrorCode.TIME_CAPSULE_FAVORITE_LIMIT_EXCEEDED -> {
                            postSideEffect(ShowFavoriteFailToast)
                        }

                        else -> {
                            Logger.e("setFavorite error, code: $code, throwable: $throwable")
                            // todo: add error ui
                        }
                    }
                },
            )
        }
}
