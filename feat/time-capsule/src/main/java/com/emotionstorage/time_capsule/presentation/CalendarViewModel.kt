package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.useCase.chat.GetChatRoomIdUseCase
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.repo.SetFavoriteResult
import com.emotionstorage.domain.useCase.dailyReport.GetDailyReportOfDateUseCase
import com.emotionstorage.domain.useCase.key.GetKeyCountUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsuleDatesUseCase
import com.emotionstorage.domain.useCase.timeCapsule.GetTimeCapsulesOfDateUseCase
import com.emotionstorage.domain.useCase.timeCapsule.SetFavoriteTimeCapsuleUseCase
import com.emotionstorage.time_capsule.presentation.CalendarSideEffect.ShowToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import org.orbitmvi.orbit.ContainerHost
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class CalendarState(
    val keyCount: Int = 0,
    val madeTimeCapsuleToday: Boolean = false,
    // calendar states
    val calendarYearMonth: YearMonth = YearMonth.now(),
    val timeCapsuleDates: List<LocalDate> = emptyList(),
    val calendarDate: LocalDate? = null,
    // bottom sheet states
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    val dailyReportId: String? = null,
    val isNewDailyReport: Boolean = false,
)

sealed class CalendarAction {
    // init screen state
    object Initiate : CalendarAction()

    // set calendar year month & get time capsule dates
    data class SelectCalendarYearMonth(
        val yearMonth: YearMonth,
    ) : CalendarAction()

    // set calendar date & get bottom sheet states
    data class SelectCalendarDate(
        val date: LocalDate,
    ) : CalendarAction()

    data class ToggleTimeCapsuleFavorite(
        val id: String,
    ) : CalendarAction()

    // reset bottom sheet states
    object ClearBottomSheet : CalendarAction()

    // get chat room id
    object EnterChat : CalendarAction()
}

sealed class CalendarSideEffect {
    object ShowTimeCapsuleBottomSheet : CalendarSideEffect()

    data class ShowToast(
        val toast: CalendarToast,
    ) : CalendarSideEffect() {
        enum class CalendarToast(
            val message: String,
        ) {
            FAVORITE_ADDED("즐겨찾기가 설정되었습니다."),
            FAVORITE_REMOVED("즐겨찾기가 해제되었습니다."),
            FAVORITE_FULL("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요."),
        }
    }

    data class EnterCharRoomSuccess(
        val roomId: String,
    ) : CalendarSideEffect()
}

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val getKeyCount: GetKeyCountUseCase,
    private val getTimeCapsuleDates: GetTimeCapsuleDatesUseCase,
    private val getTimeCapsulesOfDate: GetTimeCapsulesOfDateUseCase,
    private val getDailyReportOfDate: GetDailyReportOfDateUseCase,
    private val setFavorite: SetFavoriteTimeCapsuleUseCase,
    private val getChatRoomId: GetChatRoomIdUseCase,
) : ViewModel(),
    ContainerHost<CalendarState, CalendarSideEffect> {
    override val container: Container<CalendarState, CalendarSideEffect> =
        container(CalendarState())

    fun onAction(action: CalendarAction) {
        when (action) {
            is CalendarAction.Initiate -> {
                handleInitiate()
            }

            is CalendarAction.SelectCalendarYearMonth -> {
                handleSelectCalendarYearMonth(action.yearMonth)
            }

            is CalendarAction.SelectCalendarDate -> {
                handleSelectCalendarDate(action.date)
            }

            is CalendarAction.ToggleTimeCapsuleFavorite -> {
                handleToggleFavorite(action.id)
            }

            is CalendarAction.ClearBottomSheet -> {
                handleClearBottomSheet()
            }

            is CalendarAction.EnterChat -> {
                handleEnterChat()
            }
        }
    }

    private fun handleInitiate() =
        intent {
            coroutineScope {
                // init key count
                launch {
                    collectDataState(
                        flow = getKeyCount(),
                        onSuccess = { data ->
                            reduce {
                                state.copy(keyCount = data)
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("handleInitKey error: $throwable")
                            reduce {
                                state.copy(keyCount = 0)
                            }
                        },
                    )
                }

                // init calendar dates
                launch {
                    collectDataState(
                        flow = getTimeCapsuleDates(state.calendarYearMonth),
                        onSuccess = { data ->
                            reduce {
                                state.copy(
                                    timeCapsuleDates = data,
                                )
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("getTimeCapsuleDates error, $throwable")
                            reduce {
                                state.copy(
                                    timeCapsuleDates = emptyList(),
                                )
                            }
                        },
                    )
                }

                // init madeTimeCapsuleToday
                launch {
                    collectDataState(
                        flow = getTimeCapsulesOfDate(LocalDate.now()),
                        onSuccess = { data ->
                            reduce {
                                state.copy(madeTimeCapsuleToday = data.isNotEmpty())
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $throwable")
                            reduce {
                                state.copy(madeTimeCapsuleToday = false)
                            }
                        },
                    )
                }
            }

            // show bottom sheet if calendarDate is not null
            if (state.calendarDate != null) {
                postSideEffect(CalendarSideEffect.ShowTimeCapsuleBottomSheet)
            }
        }

    private fun handleSelectCalendarYearMonth(yearMonth: YearMonth) =
        intent {
            collectDataState(
                flow = getTimeCapsuleDates(yearMonth),
                onSuccess = { data ->
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            timeCapsuleDates = data,
                        )
                    }
                },
                onError = { throwable, _ ->
                    Logger.e("getTimeCapsuleDates error, $throwable")
                    reduce {
                        state.copy(
                            calendarYearMonth = yearMonth,
                            timeCapsuleDates = emptyList(),
                        )
                    }
                },
            )
        }

    private fun handleSelectCalendarDate(date: LocalDate) =
        intent {
            reduce {
                state.copy(calendarDate = date)
            }

            // todo: return if selected date is not in time capsule dates
            coroutineScope {
                // get time capsules of date
                launch {
                    collectDataState(
                        flow = getTimeCapsulesOfDate(date),
                        onSuccess = { data ->
                            reduce {
                                state.copy(timeCapsules = data.map { TimeCapsuleMapper.toUi(it) })
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("CalendarViewModel: handleGetTimeCapsulesOfDate error: $throwable")
                            reduce {
                                state.copy(timeCapsules = emptyList())
                            }
                        },
                    )
                }

                // get daily report of day
                launch {
                    collectDataState(
                        flow = getDailyReportOfDate(date),
                        onSuccess = { data ->
                            reduce {
                                state.copy(
                                    dailyReportId = data.dailyReportId,
                                    isNewDailyReport = data.isNewDailyReport,
                                )
                            }
                        },
                        onError = { throwable, _ ->
                            Logger.e("CalendarViewModel: handleGetDailyReportOfDate error: $throwable")
                            reduce {
                                state.copy(
                                    dailyReportId = null,
                                    isNewDailyReport = false,
                                )
                            }
                        },
                    )
                }
            }

            postSideEffect(CalendarSideEffect.ShowTimeCapsuleBottomSheet)
        }

    private fun handleToggleFavorite(id: String) =
        intent {
            if (state.timeCapsules.find { it.id == id } == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }
            val newIsFavorite = !state.timeCapsules.find { it.id == id }!!.isFavorite

            suspend fun updateFavorite(
                id: String,
                isFavorite: Boolean,
            ) = reduce {
                state.copy(
                    timeCapsules =
                        state.timeCapsules.map {
                            if (it.id == id) {
                                it.copy(isFavorite = isFavorite)
                            } else {
                                it
                            }
                        },
                )
            }

            coroutineScope {
                collectDataState(
                    flow = setFavorite(id, newIsFavorite),
                    onSuccess = {
                        if (it == SetFavoriteResult.ADDED) {
                            postSideEffect(ShowToast(ShowToast.CalendarToast.FAVORITE_ADDED))
                            updateFavorite(id, true)
                        } else if (it == SetFavoriteResult.REMOVED) {
                            postSideEffect(ShowToast(ShowToast.CalendarToast.FAVORITE_REMOVED))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == SetFavoriteResult.FULL) {
                            postSideEffect(ShowToast(ShowToast.CalendarToast.FAVORITE_FULL))
                        }
                    },
                )
            }
        }

    private fun handleClearBottomSheet() =
        intent {
            // clear bottom sheet states
            reduce {
                state.copy(
                    calendarDate = null,
                    timeCapsules = emptyList(),
                    dailyReportId = null,
                    isNewDailyReport = false,
                )
            }
        }

    private fun handleEnterChat() =
        intent {
            collectDataState(
                flow = getChatRoomId(),
                onSuccess = { data ->
                    postSideEffect(CalendarSideEffect.EnterCharRoomSuccess(data))
                },
                onError = { throwable, _ ->
                    Logger.e("CalendarViewModel: handleEnterChat error: $throwable")
                },
            )
        }
}
