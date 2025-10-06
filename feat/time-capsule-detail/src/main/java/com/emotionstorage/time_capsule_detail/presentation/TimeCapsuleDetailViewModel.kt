package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase.ToggleToastResult
import com.emotionstorage.domain.useCase.key.GetRequiredKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.DeleteTimeCapsuleUseCase
import com.emotionstorage.domain.useCase.timeCapsule.OpenArrivedTimeCapsuleUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SaveTimeCapsuleNoteUseCase
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.Init
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnDeleteTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnDeleteTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnExitTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnExpireTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnNoteChanged
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveChangeTrigger
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveNote
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnToggleFavorite
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnUnlockTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowDeleteModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExitModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowExpiredModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowSaveChangesModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowToast.TimeCapsuleDetailToast
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal.UnlockModalState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class TimeCapsuleDetailState(
    val timeCapsule: TimeCapsule? = null,
    val note: String = "",
    val isNoteChanged: Boolean = false,
)

sealed class TimeCapsuleDetailAction {
    data class Init(
        val id: String,
    ) : TimeCapsuleDetailAction()

    data class OnToggleFavorite(
        val id: String,
    ) : TimeCapsuleDetailAction()

    data class OnDeleteTimeCapsule(
        val id: String,
    ) : TimeCapsuleDetailAction()

    data class OnUnlockTimeCapsule(
        val id: String,
    ) : TimeCapsuleDetailAction()

    data class OnNoteChanged(
        val note: String,
    ) : TimeCapsuleDetailAction()

    data class OnSaveNote(
        val id: String,
    ) : TimeCapsuleDetailAction()

    object OnDeleteTrigger : TimeCapsuleDetailAction()

    object OnExitTrigger : TimeCapsuleDetailAction()

    object OnExpireTrigger : TimeCapsuleDetailAction()

    object OnSaveChangeTrigger : TimeCapsuleDetailAction()
}

sealed class TimeCapsuleDetailSideEffect {
    object GetTimeCapsuleFail : TimeCapsuleDetailSideEffect()

    object OpenTimeCapsuleFail : TimeCapsuleDetailSideEffect()

    object DeleteTimeCapsuleSuccess : TimeCapsuleDetailSideEffect()

    data class ShowUnlockModal(
        val modalState: UnlockModalState,
    ) : TimeCapsuleDetailSideEffect() {
        data class UnlockModalState(
            val keyCount: Int = 0,
            val requiredKeyCount: Int = 0,
            val arriveAt: LocalDateTime = LocalDateTime.now(),
        )
    }

    object ShowExitModal : TimeCapsuleDetailSideEffect()

    object ShowExpiredModal : TimeCapsuleDetailSideEffect()

    object ShowDeleteModal : TimeCapsuleDetailSideEffect()

    object ShowSaveChangesModal : TimeCapsuleDetailSideEffect()

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
    private val openArrivedTimeCapsule: OpenArrivedTimeCapsuleUseCase,
    private val getKeyCount: GetKeyCountUseCase,
    private val getRequiredKeyCount: GetRequiredKeyCountUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val saveNote: SaveTimeCapsuleNoteUseCase,
    private val deleteTimeCapsule: DeleteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> {
    override val container: Container<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> =
        container(TimeCapsuleDetailState())

    fun onAction(action: TimeCapsuleDetailAction) {
        when (action) {
            is Init -> {
                handleInit(action.id)
            }

            is OnToggleFavorite -> {
                handleToggleFavorite(action.id)
            }

            is OnDeleteTimeCapsule -> {
                handleDeleteTimeCapsule(action.id)
            }

            is OnUnlockTimeCapsule -> {
                handleUnlockTimeCapsule(action.id)
            }

            is OnNoteChanged -> {
                handleNoteChanged(action.note)
            }

            is OnSaveNote -> {
                handleSaveNote(action.id)
            }

            is OnDeleteTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowDeleteModal)
                }
            }

            is OnExitTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowExitModal)
                }
            }

            is OnExpireTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowExpiredModal)
                }
            }

            is OnSaveChangeTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowSaveChangesModal)
                }
            }
        }
    }

    private fun handleInit(id: String) =
        intent {
            collectDataState(
                flow = getTimeCapsuleById(id),
                onSuccess = {
                    reduce {
                        state.copy(
                            timeCapsule = it,
                            note = it.note ?: "",
                            isNoteChanged = false,
                        )
                    }
                    if (it.status == TimeCapsule.STATUS.ARRIVED) {
                        openArrivedTimeCapsule(it)
                    }
                    if (it.status == TimeCapsule.STATUS.LOCKED) {
                        triggerUnlockModal()
                    }
                },
                onError = { throwable, data ->
                    Logger.e("getTimeCapsuleById error: $throwable")
                    reduce {
                        state.copy(timeCapsule = null, note = "")
                    }
                    postSideEffect(TimeCapsuleDetailSideEffect.GetTimeCapsuleFail)
                },
            )
        }

    @OptIn(OrbitExperimental::class)
    private suspend fun openArrivedTimeCapsule(timeCapsule: TimeCapsule) =
        subIntent {
            collectDataState(
                flow = openArrivedTimeCapsule(timeCapsule.id),
                onSuccess = {
                    reduce {
                        state.copy(
                            timeCapsule = timeCapsule.copy(status = TimeCapsule.STATUS.OPENED),
                        )
                    }
                },
                onError = { throwable, data ->
                    Logger.e("openArrivedTimeCapsule error: $throwable")
                    postSideEffect(TimeCapsuleDetailSideEffect.OpenTimeCapsuleFail)
                },
            )
        }

    @OptIn(OrbitExperimental::class)
    private suspend fun triggerUnlockModal() =
        subIntent {
            if (state.timeCapsule == null || state.timeCapsule?.arriveAt == null) {
                Logger.e("invalid time capsule, ${state.timeCapsule}")
                return@subIntent
            }

            // get required key count
            collectDataState(
                flow = getRequiredKeyCount(state.timeCapsule?.arriveAt!!.toLocalDate()),
                onSuccess = { requiredKeyCount ->
                    // get key count
                    collectDataState(
                        flow = getKeyCount(),
                        onSuccess = { keyCount ->
                            postSideEffect(
                                ShowUnlockModal(
                                    UnlockModalState(
                                        keyCount = keyCount,
                                        requiredKeyCount = requiredKeyCount,
                                        arriveAt = state.timeCapsule?.arriveAt!!,
                                    ),
                                ),
                            )
                        },
                    )
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
                        postSideEffect(ShowToast(TimeCapsuleDetailToast.FAVORITE_ADDED))
                        reduce {
                            state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = true))
                        }
                    } else if (it == ToggleToastResult.FAVORITE_REMOVED) {
                        postSideEffect(ShowToast(TimeCapsuleDetailToast.FAVORITE_REMOVED))
                        reduce {
                            state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = false))
                        }
                    }
                },
                onError = { throwable, data ->
                    if (data == ToggleToastResult.FAVORITE_FULL) {
                        postSideEffect(ShowToast(TimeCapsuleDetailToast.FAVORITE_FULL))
                    }
                },
            )
        }

    private fun handleDeleteTimeCapsule(id: String) =
        intent {
            collectDataState(
                flow = deleteTimeCapsule(id),
                onSuccess = {
                    postSideEffect(DeleteTimeCapsuleSuccess)
                },
                onError = { throwable, _ ->
                    Logger.e("deleteTimeCapsule error: $throwable")
                }
            )
        }

    private fun handleUnlockTimeCapsule(id: String) =
        intent {
            // todo: unlock time capsule
            reduce {
                state.copy(
                    timeCapsule =
                        state.timeCapsule?.copy(
                            status = TimeCapsule.STATUS.ARRIVED,
                        ),
                )
            }
        }

    private fun handleNoteChanged(note: String) =
        intent {
            reduce {
                state.copy(
                    note = note,
                    isNoteChanged = state.timeCapsule?.note != note,
                )
            }
        }

    private fun handleSaveNote(id: String) =
        intent {
            if (!state.isNoteChanged) return@intent

            collectDataState(
                flow = saveNote(id, state.note),
                onSuccess = {
                    reduce {
                        state.copy(
                            // stub logic for note save
                            timeCapsule = state.timeCapsule?.copy(note = state.note),
                            isNoteChanged = false,
                        )
                    }
                },
                onError = { throwable, data ->
                    Logger.e("saveNote error: $throwable")
                },
            )
        }
}
