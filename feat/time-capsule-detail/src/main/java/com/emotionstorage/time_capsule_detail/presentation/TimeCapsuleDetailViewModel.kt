package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase.ToggleToastResult
import com.emotionstorage.time_capsule_detail.domain.useCase.GetRequiredKeyCountUseCase
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class TimeCapsuleDetailState(
    val keyCount: Int = 0,
    val requiredKeyCount: Int? = null,
    val timeCapsule: TimeCapsule? = null,
    val isMindNoteChanged: Boolean = false,
)

sealed class TimeCapsuleDetailAction {
    data class Init(
        val id: String,
    ) : TimeCapsuleDetailAction()

    object OnLockedTrigger : TimeCapsuleDetailAction()

    object OnExpireTrigger : TimeCapsuleDetailAction()

    data class OnToggleFavorite(
        val id: String,
    ) : TimeCapsuleDetailAction()

    object OnDeleteTrigger : TimeCapsuleDetailAction()

    data class OnDeleteTimeCapsule(
        val id: String,
    ) : TimeCapsuleDetailAction()
}

sealed class TimeCapsuleDetailSideEffect {
    object DeleteTimeCapsuleSuccess : TimeCapsuleDetailSideEffect()

    object ShowUnlockModal : TimeCapsuleDetailSideEffect()

    object ShowExpiredModal : TimeCapsuleDetailSideEffect()

    object ShowDeleteModal : TimeCapsuleDetailSideEffect()

    data class ShowToast(
        val toast: TimeCapsuleDetailToast,
    ) : TimeCapsuleDetailSideEffect() {
        enum class TimeCapsuleDetailToast(
            val message: String,
        ) {
            FAVORITE_ADDED("즐겨찾기가 설정되었습니다."),
            FAVORITE_REMOVED("즐겨찾기가 해제되었습니다."),
            FAVORITE_FULL("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요."),
        }
    }
}

@HiltViewModel
class TimeCapsuleDetailViewModel @Inject constructor(
    private val getTimeCapsuleById: GetTimeCapsuleByIdUseCase,
    private val getKeyCount: GetKeyCountUseCase,
    private val getRequiredKeyCount: GetRequiredKeyCountUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) : ViewModel(),
    ContainerHost<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> {
    override val container: Container<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> =
        container(TimeCapsuleDetailState())

    fun onAction(action: TimeCapsuleDetailAction) {
        when (action) {
            is TimeCapsuleDetailAction.Init -> {
                handleInit(action.id)
            }

            is TimeCapsuleDetailAction.OnLockedTrigger -> {
                handleOnLockedTrigger()
            }

            is TimeCapsuleDetailAction.OnExpireTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(TimeCapsuleDetailSideEffect.ShowExpiredModal)
                }
            }

            is TimeCapsuleDetailAction.OnToggleFavorite -> {
                handleToggleFavorite(action.id)
            }

            is TimeCapsuleDetailAction.OnDeleteTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(TimeCapsuleDetailSideEffect.ShowDeleteModal)
                }
            }

            is TimeCapsuleDetailAction.OnDeleteTimeCapsule -> {
                handleDeleteTimeCapsule(action.id)
            }
        }
    }

    private fun handleInit(id: String) =
        intent {
            // get key count
            collectDataState(
                flow = getKeyCount(),
                onSuccess = {
                    reduce {
                        state.copy(
                            keyCount = it,
                        )
                    }
                }
            )

            // get time capsule detail
            collectDataState(
                flow = getTimeCapsuleById(id),
                onSuccess = {
                    reduce {
                        state.copy(
                            timeCapsule = it,
                        )
                    }
                },
                onError = { throwable, data ->
                    Logger.e("getTimeCapsuleById error: $throwable")
                    // todo: handle error
                },
            )
        }

    private fun handleOnLockedTrigger() =
        intent {
            if (state.timeCapsule == null) {
                Logger.e("time capsule is null")
                return@intent
            }
            if (state.timeCapsule?.status != TimeCapsule.STATUS.LOCKED) {
                Logger.d("time capsule is not locked")
                return@intent
            }
            if (state.timeCapsule?.arriveAt == null) {
                Logger.e("arriveAt is null")
                return@intent
            }

            // get required key count
            collectDataState(
                flow = getRequiredKeyCount(state.timeCapsule?.arriveAt!!.toLocalDate()),
                onSuccess = {
                    reduce {
                        state.copy(
                            requiredKeyCount = it,
                        )
                    }
                    postSideEffect(TimeCapsuleDetailSideEffect.ShowUnlockModal)
                },
                onError = { throwable, data ->
                    Logger.e("getRequiredKeyCount error: $throwable")
                    // todo: handle error
                },
            )
        }


    private fun handleToggleFavorite(id: String) =
        intent {
            collectDataState(
                flow = toggleFavorite(id),
                onSuccess = {
                    if (it == ToggleToastResult.FAVORITE_ADDED) {
                        postSideEffect(ShowToast(ShowToast.TimeCapsuleDetailToast.FAVORITE_ADDED))
                        reduce {
                            state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = true))
                        }
                    } else if (it == ToggleToastResult.FAVORITE_REMOVED) {
                        postSideEffect(ShowToast(ShowToast.TimeCapsuleDetailToast.FAVORITE_REMOVED))
                        reduce {
                            state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = false))
                        }
                    }
                },
                onError = { throwable, data ->
                    if (data == ToggleToastResult.FAVORITE_FULL) {
                        postSideEffect(ShowToast(ShowToast.TimeCapsuleDetailToast.FAVORITE_FULL))
                    }
                },
            )

        }

    private fun handleDeleteTimeCapsule(id: String) =
        intent {
            // todo: delete time capsule
            postSideEffect(TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess)
        }
}
