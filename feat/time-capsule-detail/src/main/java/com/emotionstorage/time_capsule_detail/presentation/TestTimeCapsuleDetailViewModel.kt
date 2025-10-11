package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
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

sealed class TestTimeCapsuleDetailAction {
    data class Init(
        val status: TimeCapsule.Status,
    ) : TestTimeCapsuleDetailAction()

    object OnToggleFavorite : TestTimeCapsuleDetailAction()

    object OnDeleteTimeCapsule : TestTimeCapsuleDetailAction()

    object OnUnlockTimeCapsule : TestTimeCapsuleDetailAction()

    data class OnNoteChanged(
        val note: String,
    ) : TestTimeCapsuleDetailAction()

    object OnSaveNote : TestTimeCapsuleDetailAction()

    object OnDeleteTrigger : TestTimeCapsuleDetailAction()

    object OnExitTrigger : TestTimeCapsuleDetailAction()

    object OnExpireTrigger : TestTimeCapsuleDetailAction()

    object OnSaveChangeTrigger : TestTimeCapsuleDetailAction()
}

@HiltViewModel
class TestTimeCapsuleDetailViewModel @Inject constructor(
    private val getTimeCapsuleById: GetTimeCapsuleByIdUseCase,
    private val openArrivedTimeCapsule: OpenArrivedTimeCapsuleUseCase,
    private val getKeyCount: GetKeyCountUseCase,
    private val getRequiredKeyCount: GetRequiredKeyCountUseCase,
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
    private val saveNote: SaveTimeCapsuleNoteUseCase,
    private val deleteTimeCapsule: DeleteTimeCapsuleUseCase,
) : ViewModel(),
    ContainerHost<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> {
    override val container: Container<TimeCapsuleDetailState, TimeCapsuleDetailSideEffect> =
        container(TimeCapsuleDetailState())

    fun onAction(action: TestTimeCapsuleDetailAction) {
        when (action) {
            is TestTimeCapsuleDetailAction.Init -> {
                handleInit(action.status)
            }

            is TestTimeCapsuleDetailAction.OnToggleFavorite -> {
                handleToggleFavorite()
            }

            is TestTimeCapsuleDetailAction.OnDeleteTimeCapsule -> {
                handleDeleteTimeCapsule()
            }

            is TestTimeCapsuleDetailAction.OnUnlockTimeCapsule -> {
                handleUnlockTimeCapsule()
            }

            is TestTimeCapsuleDetailAction.OnNoteChanged -> {
                handleNoteChanged(action.note)
            }

            is TestTimeCapsuleDetailAction.OnSaveNote -> {
                handleSaveNote()
            }

            is TestTimeCapsuleDetailAction.OnDeleteTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowDeleteModal)
                }
            }

            is TestTimeCapsuleDetailAction.OnExitTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowExitModal)
                }
            }

            is TestTimeCapsuleDetailAction.OnExpireTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowExpiredModal)
                }
            }

            is TestTimeCapsuleDetailAction.OnSaveChangeTrigger -> {
                // trigger side effect
                intent {
                    postSideEffect(ShowSaveChangesModal)
                }
            }
        }
    }

    private fun handleInit(status: TimeCapsule.Status) =
        intent {
            reduce {
                state.copy(
                    timeCapsule =
                        TimeCapsule(
                            id = "id",
                            status = status,
                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                            summary =
                                "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어." +
                                    "그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. " +
                                    "나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
                            emotions =
                                listOf(
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE14",
                                        label = "서운함",
                                        percentage = 30.0f,
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE0A",
                                        label = "고마움",
                                        percentage = 30.0f,
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83E\uDD70",
                                        label = "안정감",
                                        percentage = 80.0f,
                                    ),
                                ),
                            comments =
                                listOf(
                                    "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                                    "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                                    "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요.",
                                ),
                            note =
                                "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. " +
                                    "사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
                            logs = emptyList(),
                            createdAt =
                                when (status) {
                                    TimeCapsule.Status.TEMPORARY -> {
                                        LocalDateTime.now().minusMinutes(90)
                                    }

                                    else -> LocalDateTime.now().minusDays(3)
                                },
                            arriveAt =
                                when (status) {
                                    TimeCapsule.Status.TEMPORARY -> {
                                        null
                                    }

                                    TimeCapsule.Status.LOCKED -> {
                                        LocalDateTime.now().plusDays(3)
                                    }

                                    else -> LocalDateTime.now().minusDays(1)
                                },
                            updatedAt = LocalDateTime.now(),
                        ),
                    note =
                        "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. " +
                            "사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
                    isNoteChanged = false,
                )
            }
            if (status == TimeCapsule.Status.LOCKED) {
                triggerUnlockModal()
            }
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

    private fun handleToggleFavorite() =
        intent {
            if (state.timeCapsule == null) {
                return@intent
            }

            if (state.timeCapsule!!.isFavorite) {
                postSideEffect(ShowToast(TimeCapsuleDetailToast.FAVORITE_REMOVED))
                reduce {
                    state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = false))
                }
            } else {
                postSideEffect(ShowToast(TimeCapsuleDetailToast.FAVORITE_ADDED))
                reduce {
                    state.copy(timeCapsule = state.timeCapsule?.copy(isFavorite = true))
                }
            }
        }

    private fun handleDeleteTimeCapsule() =
        intent {
            postSideEffect(DeleteTimeCapsuleSuccess)
        }

    private fun handleUnlockTimeCapsule() =
        intent {
            reduce {
                state.copy(
                    timeCapsule =
                        state.timeCapsule?.copy(
                            status = TimeCapsule.Status.ARRIVED,
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

    private fun handleSaveNote() =
        intent {
            if (!state.isNoteChanged) return@intent
            reduce {
                state.copy(
                    // stub logic for note save
                    timeCapsule = state.timeCapsule?.copy(note = state.note),
                    isNoteChanged = false,
                )
            }
        }
}
