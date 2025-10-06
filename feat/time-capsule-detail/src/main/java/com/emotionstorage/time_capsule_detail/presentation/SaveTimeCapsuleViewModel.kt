package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleByIdUseCase
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleSideEffect.ShowToast
import com.emotionstorage.time_capsule_detail.presentation.SaveTimeCapsuleState.ArriveAfter
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class SaveTimeCapsuleState(
    val id: String = "",
    val isNewTimeCapsule: Boolean = false,
    val emotions: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val expireAt: LocalDateTime= LocalDateTime.now(),
    val saveAt: LocalDateTime = LocalDateTime.now(),
    val arriveAfter: ArriveAfter? = null,
    val arriveAt: LocalDateTime? = null,
) {
    enum class ArriveAfter(
        val label: String,
    ) {
        AFTER_24HOURS("24시간 후"),
        AFTER_3DAYS("3일 후"),
        AFTER_1WEEK("일주일 후"),
        AFTER_15DAYS("15일 후"),
        AFTER_30DAYS("30일 후"),
        AFTER_1YEAR("1년 후"),
        AFTER_CUSTOM("직접선택"),
    }
}

sealed class SaveTimeCapsuleAction {
    data class Init(
        val id: String,
        val isNewTimeCapsule: Boolean,
    ) : SaveTimeCapsuleAction()

    // select open date from grid item
    data class SelectArriveAfter(
        val arriveAfter: ArriveAfter?,
    ) : SaveTimeCapsuleAction()

    // select open date from calendar bottom sheet
    data class SelectArriveAt(
        val arriveAt: LocalDate,
    ) : SaveTimeCapsuleAction()

    object SaveTimeCapsule : SaveTimeCapsuleAction()
}

sealed class SaveTimeCapsuleSideEffect {
    data class ShowToast(
        val toast: String = "아직 보관을 확정하지 않은 감정이에요.\n오늘을 기준으로 타임캡슐\n회고 날짜를 지정해주세요.",
    ) : SaveTimeCapsuleSideEffect()

//    data class ShowDatePickerBottomSheet(
//        val yearMonth: YearMonth,
//        val minDate: LocalDate,
//        val maxDate: LocalDate,
//        val date: LocalDate? = null,
//    ) : SaveTimeCapsuleSideEffect()

    // todo: show year moth picker bottom sheet

    object SaveTimeCapsuleSuccess : SaveTimeCapsuleSideEffect()
}

@HiltViewModel
class SaveTimeCapsuleViewModel @Inject constructor(
    private val getTimeCapsuleById: GetTimeCapsuleByIdUseCase,
) : ViewModel(),
    ContainerHost<SaveTimeCapsuleState, SaveTimeCapsuleSideEffect> {
    override val container: Container<SaveTimeCapsuleState, SaveTimeCapsuleSideEffect> =
        container(SaveTimeCapsuleState())

    fun onAction(action: SaveTimeCapsuleAction) {
        when (action) {
            is SaveTimeCapsuleAction.Init -> {
                handleInit(action.id, action.isNewTimeCapsule)
            }

            is SaveTimeCapsuleAction.SelectArriveAfter -> {
                handleSelectArriveAfter(action.arriveAfter)
            }

            is SaveTimeCapsuleAction.SelectArriveAt -> {
                handleSelectArriveAt(action.arriveAt)
            }

            is SaveTimeCapsuleAction.SaveTimeCapsule -> {
                handleSaveTimeCapsule()
            }
        }
    }

    private fun handleInit(
        id: String,
        isNewTimeCapsule: Boolean,
    ) = intent {
        collectDataState(
            flow = getTimeCapsuleById(id),
            onSuccess = {
                reduce {
                    state.copy(
                        id = id,
                        isNewTimeCapsule = isNewTimeCapsule,
                        // todo: change domain model's emotion list type
                        emotions = listOf("\uD83D\uDE14 서운함", "\uD83D\uDE0A 고마움", "\uD83E\uDD70 안정감"),
                        createdAt = it.createdAt,
                        // 보관일 = 새 타임캡슐인 경우, 생성 시점 / 일시저장 타임캡슐인 경우, 화면 진입 시점
                        saveAt = if (isNewTimeCapsule) it.createdAt else LocalDateTime.now(),
                        expireAt = it.expireAt,
                        arriveAfter = null,
                        arriveAt = null,
                    )
                }

                if (!isNewTimeCapsule) {
                    postSideEffect(ShowToast())
                }
            },
        )
    }

    private fun handleSelectArriveAfter(arriveAfter: ArriveAfter?) =
        intent {
            when (arriveAfter) {
                null -> {
                    reduce {
                        state.copy(
                            arriveAfter = null,
                            arriveAt = null,
                        )
                    }
                }

                ArriveAfter.AFTER_24HOURS -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusHours(24),
                        )
                    }
                }

                ArriveAfter.AFTER_3DAYS -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusDays(3),
                        )
                    }
                }

                ArriveAfter.AFTER_1WEEK -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusDays(7),
                        )
                    }
                }

                ArriveAfter.AFTER_15DAYS -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusDays(15),
                        )
                    }
                }

                ArriveAfter.AFTER_30DAYS -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusDays(30),
                        )
                    }
                }

                ArriveAfter.AFTER_1YEAR -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = state.saveAt.plusYears(1),
                        )
                    }
                }

                ArriveAfter.AFTER_CUSTOM -> {
                    reduce {
                        state.copy(
                            arriveAfter = arriveAfter,
                            arriveAt = null,
                        )
                    }

                    // trigger local date selection bottom sheet
//                    postSideEffect(
//                        ShowDatePickerBottomSheet(
//                            yearMonth = if (state.arriveAt != null) YearMonth.of(
//                                state.arriveAt!!.toLocalDate().year,
//                                state.arriveAt!!.toLocalDate().month
//                            ) else YearMonth.now(),
//                            date = state.arriveAt?.toLocalDate(),
//                            minDate = state.saveAt.toLocalDate().plusDays(1),
//                            maxDate = state.saveAt.toLocalDate().plusYears(1)
//                        )
//                    )
                }
            }
        }

    private fun handleSelectArriveAt(arriveAt: LocalDate) =
        intent {
            if (arriveAt.isBefore(state.saveAt.toLocalDate())) {
                Logger.e("Timecapsule open date can not be before save date")
                return@intent
            }
            if (arriveAt.isAfter(state.saveAt.toLocalDate().plusYears(1))) {
                Logger.e("Timecapsule open date can not be after 1 year from save date")
                return@intent
            }

            reduce {
                state.copy(
                    arriveAfter = ArriveAfter.AFTER_CUSTOM,
                    arriveAt = LocalDateTime.of(arriveAt, state.saveAt.toLocalTime()),
                )
            }
        }

    private fun handleSaveTimeCapsule() =
        intent {
            // todo: call save open date use case
            postSideEffect(
                SaveTimeCapsuleSideEffect.SaveTimeCapsuleSuccess,
            )
        }
}
