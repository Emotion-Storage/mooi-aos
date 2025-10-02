package com.emotionstorage.time_capsule_detail.presentation

import androidx.lifecycle.ViewModel
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
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val saveAt: LocalDateTime = LocalDateTime.now(),
    val arriveAfter: ArriveAfter? = null,
    val arriveAt: LocalDateTime? = null,
) {
    enum class ArriveAfter {
        AFTER_24HOURS,
        AFTER_3DAYS,
        AFTER_1WEEK,
        AFTER_15DAYS,
        AFTER_30DAYS,
        AFTER_1YEAR,
        AFTER_CUSTOM,
    }
}

sealed class SaveTimeCapsuleAction {
    data class Init(
        val id: String,
        val isNewTimeCapsule: Boolean,
        val createdAt: LocalDateTime,
    ) : SaveTimeCapsuleAction()

    // select open date from grid item
    data class SelectArriveAfter(
        val arriveAfter: ArriveAfter,
    ) : SaveTimeCapsuleAction()

    // select open date from calendar bottom sheet
    data class SelectArriveAfterCustom(
        val arriveAt: LocalDate,
    ) : SaveTimeCapsuleAction()

    object SaveTimeCapsule : SaveTimeCapsuleAction()
}

sealed class SaveTimeCapsuleSideEffect {
    object SaveTimeCapsuleSuccess : SaveTimeCapsuleSideEffect()
}

@HiltViewModel
class SaveTimeCapsuleViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<SaveTimeCapsuleState, SaveTimeCapsuleSideEffect> {
        override val container: Container<SaveTimeCapsuleState, SaveTimeCapsuleSideEffect> =
            container(SaveTimeCapsuleState())

        fun onAction(action: SaveTimeCapsuleAction) {
            when (action) {
                is SaveTimeCapsuleAction.Init -> {
                    handleInit(action.id, action.isNewTimeCapsule, action.createdAt)
                }

                is SaveTimeCapsuleAction.SelectArriveAfter -> {
                    handleSelectArriveAfter(action.arriveAfter)
                }

                is SaveTimeCapsuleAction.SelectArriveAfterCustom -> {
                    handleSelectArriveAfterCustom(action.arriveAt)
                }

                is SaveTimeCapsuleAction.SaveTimeCapsule -> {
                    handleSaveTimeCapsule()
                }
            }
        }

        private fun handleInit(
            id: String,
            isNewTimeCapsule: Boolean,
            createdAt: LocalDateTime,
        ) = intent {
            reduce {
                state.copy(
                    id = id,
                    isNewTimeCapsule = isNewTimeCapsule,
                    createdAt = createdAt,
                    // 보관일 = 새 타임캡슐인 경우, 생성 시점 / 일시저장 타임캡슐인 경우, 화면 진입 시점
                    saveAt = if (isNewTimeCapsule) createdAt else LocalDateTime.now(),
                    arriveAfter = null,
                    arriveAt = null,
                )
            }
        }

        private fun handleSelectArriveAfter(arriveAfter: ArriveAfter) =
            intent {
                when (arriveAfter) {
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
                        // 오픈일 직접 선택하는 경우는, SelectArriveAfterCustom 액션을 통해 처리
                        return@intent
                    }
                }
            }

        private fun handleSelectArriveAfterCustom(arriveAt: LocalDate) =
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
