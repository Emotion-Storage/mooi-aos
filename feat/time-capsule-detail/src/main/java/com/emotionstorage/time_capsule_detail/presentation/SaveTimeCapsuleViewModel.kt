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
import java.time.YearMonth
import javax.inject.Inject

data class SaveTimeCapsuleState(
    val isLoading: Boolean = true,
    val id: String = "",
    val isNewTimeCapsule: Boolean = false,
    val emotions: List<String> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val expireAt: LocalDateTime? = null,
    val saveAt: LocalDateTime = LocalDateTime.now(),
    val arriveAfter: ArriveAfter? = null,
    val arriveAt: LocalDateTime? = null,
    // year month state for date picker bottom sheet
    val calendarYearMonth: YearMonth = YearMonth.now(),
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

    data class SelectCalendarYearMonth(
        val yearMonth: YearMonth,
    ) : SaveTimeCapsuleAction()

    object SaveTimeCapsule : SaveTimeCapsuleAction()
}

sealed class SaveTimeCapsuleSideEffect {
    data class ShowToast(
        val toast: String = "아직 보관을 확정하지 않은 감정이에요.\n오늘을 기준으로 타임캡슐\n회고 날짜를 지정해주세요.",
    ) : SaveTimeCapsuleSideEffect()

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

            is SaveTimeCapsuleAction.SelectCalendarYearMonth -> {
                handleSelectCalendarYearMonth(action.yearMonth)
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
                        isLoading = false,
                        id = id,
                        isNewTimeCapsule = isNewTimeCapsule,
                        emotions = it.emotions.map { it.emoji + " " + it.label },
                        createdAt = it.createdAt,
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
            onError = { throwable, _ ->
                Logger.e("Error getting time capsule by id, $throwable")
                reduce {
                    state.copy(
                        isLoading = true,
                    )
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
                            calendarYearMonth = YearMonth.now(),
                        )
                    }
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
                    calendarYearMonth = YearMonth.from(arriveAt),
                )
            }
        }

    private fun handleSelectCalendarYearMonth(yearMonth: YearMonth) =
        intent {
            reduce {
                state.copy(
                    calendarYearMonth = yearMonth,
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
