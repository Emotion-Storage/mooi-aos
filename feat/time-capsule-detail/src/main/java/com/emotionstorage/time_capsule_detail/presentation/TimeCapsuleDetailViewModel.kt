package com.emotionstorage.time_capsule_detail.presentation

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.key.GetRequiredKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.DeleteTimeCapsuleUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import com.emotionstorage.domain.useCase.timeCapsule.OpenTimeCapsuleUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SaveTimeCapsuleNoteUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.Init
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnDeleteTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnNoteChanged
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnSaveNote
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailAction.OnUnlockTimeCapsule
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.DeleteTimeCapsuleSuccess
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal
import com.emotionstorage.time_capsule_detail.presentation.TimeCapsuleDetailSideEffect.ShowUnlockModal.UnlockModalState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.annotation.OrbitExperimental
import java.time.LocalDateTime
import javax.inject.Inject

data class TimeCapsuleDetailState(
    val timeCapsule: TimeCapsule? = null,
    val note: String = "",
    val isNoteChanged: Boolean = false,
)

sealed class TimeCapsuleDetailAction {
    data class Init(
        val id: Long,
    ) : TimeCapsuleDetailAction()

    data class OnUnlockTimeCapsule(
        val id: Long,
    ) : TimeCapsuleDetailAction()

    data class OnDeleteTimeCapsule(
        val id: Long,
    ) : TimeCapsuleDetailAction()

    data class OnNoteChanged(
        val note: String,
    ) : TimeCapsuleDetailAction()

    data class OnSaveNote(
        val id: Long,
        val exitAfterSave: Boolean,
    ) : TimeCapsuleDetailAction()
}

sealed class TimeCapsuleDetailSideEffect : BaseSideEffect {
    object GetTimeCapsuleFail : TimeCapsuleDetailSideEffect()

    object OpenTimeCapsuleFail : TimeCapsuleDetailSideEffect()

    object DeleteTimeCapsuleSuccess : TimeCapsuleDetailSideEffect()

    data class ShowUnlockModal(
        val modalState: UnlockModalState,
    ) : TimeCapsuleDetailSideEffect() {
        data class UnlockModalState(
            val keyCount: Int = 0,
            val requiredKeyCount: Int = 0,
            val openAt: LocalDateTime = LocalDateTime.now(),
        )
    }

    data class SaveChangesSuccess(
        val exitAfterSave: Boolean,
    ) : TimeCapsuleDetailSideEffect()
}

@HiltViewModel
class TimeCapsuleDetailViewModel @Inject constructor(
    private val getTimeCapsuleById: GetTimeCapsuleByIdUseCase,
    private val openTimeCapsule: OpenTimeCapsuleUseCase,
    private val getKeyCount: GetKeyCountUseCase,
    private val getRequiredKeyCount: GetRequiredKeyCountUseCase,
    private val saveNote: SaveTimeCapsuleNoteUseCase,
    private val deleteTimeCapsule: DeleteTimeCapsuleUseCase,
) : BaseViewModel<TimeCapsuleDetailState>(
        TimeCapsuleDetailState(),
    ) {
    fun onAction(action: TimeCapsuleDetailAction) {
        when (action) {
            is Init -> {
                handleInit(action.id)
            }

            is OnUnlockTimeCapsule -> {
                handleOpenTimeCapsule(action.id)
            }

            is OnDeleteTimeCapsule -> {
                handleDeleteTimeCapsule(action.id)
            }

            is OnNoteChanged -> {
                handleNoteChanged(action.note)
            }

            is OnSaveNote -> {
                handleSaveNote(action.id, action.exitAfterSave)
            }
        }
    }

    private fun handleInit(id: Long) =
        baseIntent {
            collectDataState(
                flow = getTimeCapsuleById(id),
                onSuccess = {
                    reduce {
                        state.copy(
                            timeCapsule = it,
                            note = it.note,
                            isNoteChanged = false,
                        )
                    }
                    if (it.status == TimeCapsule.Status.ARRIVED) {
                        handleOpenTimeCapsule(id)
                    }
                    if (it.status == TimeCapsule.Status.LOCKED) {
                        triggerUnlockModal()
                    }
                },
                onError = { throwable, code, data ->
                    Logger.e("getTimeCapsuleById error: $throwable")
                    reduce {
                        state.copy(timeCapsule = null, note = "")
                    }
                    postSideEffect(TimeCapsuleDetailSideEffect.GetTimeCapsuleFail)
                },
            )
        }

    private fun handleOpenTimeCapsule(id: Long) =
        baseIntent {
            collectDataState(
                flow = openTimeCapsule(id),
                onSuccess = {
                    Logger.d("openArrivedTimeCapsule success")
                    reduce {
                        state.copy(
                            timeCapsule = state.timeCapsule?.copy(status = TimeCapsule.Status.OPENED),
                        )
                    }
                },
                onError = { throwable, code, data ->
                    Logger.e("openArrivedTimeCapsule error: $throwable")
                    postSideEffect(TimeCapsuleDetailSideEffect.OpenTimeCapsuleFail)
                },
            )
        }

    @OptIn(OrbitExperimental::class)
    private suspend fun triggerUnlockModal() =
        subIntent {
            if (state.timeCapsule == null || state.timeCapsule?.openAt == null) {
                Logger.e("invalid time capsule, ${state.timeCapsule}")
                return@subIntent
            }

            // get required key count
            collectDataState(
                flow = getRequiredKeyCount(state.timeCapsule?.openAt!!.toLocalDate()),
                onSuccess = { requiredKeyCount ->
                    // get key count
                    val result = getKeyCount()
                    if (result is DataState.Success) {
                        postSideEffect(
                            ShowUnlockModal(
                                UnlockModalState(
                                    keyCount = result.data,
                                    requiredKeyCount = requiredKeyCount,
                                    openAt = state.timeCapsule?.openAt!!,
                                ),
                            ),
                        )
                    }
                },
                onError = { throwable, code, data ->
                    Logger.e("getRequiredKeyCount error: $throwable")
                    // todo: handle error
                },
            )
        }

    private fun handleDeleteTimeCapsule(id: Long) =
        baseIntent {
            collectDataState(
                flow = deleteTimeCapsule(id),
                onSuccess = {
                    postSideEffect(DeleteTimeCapsuleSuccess)
                },
                onError = { throwable, code, data ->
                    Logger.e("deleteTimeCapsule error: $throwable")
                    throw BaseException(
                        message = throwable.message ?: "deleteTimeCapsule error",
                        code = code,
                        cause = throwable,
                    )
                },
            )
        }

    private fun handleNoteChanged(note: String) =
        baseIntent {
            reduce {
                state.copy(
                    note = note,
                    isNoteChanged = state.timeCapsule?.note != note,
                )
            }
        }

    private fun handleSaveNote(
        id: Long,
        exitAfterSave: Boolean,
    ) = baseIntent {
        if (!state.isNoteChanged) return@baseIntent

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

                postSideEffect(
                    TimeCapsuleDetailSideEffect.SaveChangesSuccess(exitAfterSave = exitAfterSave),
                )
            },
            onError = { throwable, code, data ->
                Logger.e("saveNote error: $throwable")
                throw BaseException(
                    message = throwable.message ?: "saveNote error",
                    code = code,
                    cause = throwable,
                )
            },
        )
    }
}
