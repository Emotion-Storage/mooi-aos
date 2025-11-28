package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteSideEffect.ShowFavoriteFailToast
import com.emotionstorage.time_capsule_detail.presentation.ToggleSingleFavoriteSideEffect.ShowFavoriteSuccessToast
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class ToggleSingleFavoriteState(
    val isFavorite: Boolean = false,
)

sealed class ToggleSingleFavoriteAction {
    data class Init(
        val isFavorite: Boolean,
    ) : ToggleSingleFavoriteAction()

    data class OnToggle(
        val id: Long,
    ) : ToggleSingleFavoriteAction()
}

sealed class ToggleSingleFavoriteSideEffect {
    data class ShowFavoriteSuccessToast(
        val isFavorite: Boolean,
    ) : ToggleSingleFavoriteSideEffect()

    object ShowFavoriteFailToast : ToggleSingleFavoriteSideEffect()
}

@HiltViewModel
class ToggleSingleFavoriteViewModel @Inject constructor(
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<ToggleSingleFavoriteState, ToggleSingleFavoriteSideEffect> {
    override val container: Container<ToggleSingleFavoriteState, ToggleSingleFavoriteSideEffect> =
        container(ToggleSingleFavoriteState())

    fun onAction(action: ToggleSingleFavoriteAction) {
        when (action) {
            is ToggleSingleFavoriteAction.Init -> {
                intent {
                    reduce {
                        state.copy(isFavorite = action.isFavorite)
                    }
                }
            }

            is ToggleSingleFavoriteAction.OnToggle -> {
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handleToggleFavorite(id: Long) =
        intent {
            setFavorite(id, !state.isFavorite).handle(
                onSuccess = {
                    reduce {
                        state.copy(isFavorite = it)
                    }
                    postSideEffect(ShowFavoriteSuccessToast(it))
                },
                onError = { throwable, code, data ->
                    when (code) {
                        ErrorCode.TIME_CAPSULE_FAVORITE_LIST_FULL -> {
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
