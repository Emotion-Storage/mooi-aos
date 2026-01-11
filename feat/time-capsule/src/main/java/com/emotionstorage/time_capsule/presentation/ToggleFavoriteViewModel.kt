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

sealed class ToggleFavoriteAction {
    data class OnToggle(
        val id: Long,
        val newFavoriteState: Boolean,
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
    ContainerHost<Unit, ToggleFavoriteSideEffect> {
    override val container: Container<Unit, ToggleFavoriteSideEffect> =
        container(Unit)

    fun onAction(action: ToggleFavoriteAction) {
        when (action) {
            is ToggleFavoriteAction.OnToggle -> {
                handleToggleFavorite(action.id, action.newFavoriteState)
            }
        }
    }

    private fun handleToggleFavorite(id: Long, newFavoriteState: Boolean) =
        intent {
            setFavorite(id, newFavoriteState).handle(
                onSuccess = {
                    postSideEffect(ShowFavoriteSuccessToast(newFavoriteState))
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
