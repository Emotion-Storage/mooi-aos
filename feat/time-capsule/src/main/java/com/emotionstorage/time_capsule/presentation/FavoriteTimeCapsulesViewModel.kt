package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesState.SortOrder
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: SortOrder = SortOrder.SORT_BY_NEWEST,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
) {
    enum class SortOrder(
        val label: String,
    ) {
        SORT_BY_NEWEST("최신 날짜순"),
        SORT_BY_FAVORITE("즐겨찾기순"),
        ;

        companion object {
            fun getByLabel(label: String): SortOrder {
                return values().find { it.label == label }
                    ?: throw IllegalArgumentException("Invalid sort order label: $label")
            }
        }
    }
}

sealed class FavoriteTimeCapsulesAction {
    object PullToRefresh : FavoriteTimeCapsulesAction()

    data class SetSortOrder(
        val sortOrderLabel: String,
    ) : FavoriteTimeCapsulesAction()

    data class ToggleFavorite(
        val id: String,
    ) : FavoriteTimeCapsulesAction()
}

sealed class FavoriteTimeCapsulesSideEffect {
    data class ShowToast(
        val toast: FavoriteToast,
    ) : FavoriteTimeCapsulesSideEffect() {
        enum class FavoriteToast(
            val message: String,
        ) {
            FAVORITE_ADDED("즐겨찾기가 설정되었습니다."),
            FAVORITE_REMOVED("즐겨찾기가 해제되었습니다."),
            FAVORITE_FULL("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요."),
        }
    }
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    // private val getFavoriteTimeCapsules: GetFavoriteTimeCapsulesUseCase,
    // private val toggleFavoriteTimeCapsule: ToggleFavoriteTimeCapsuleUseCase
) : ViewModel(),
    ContainerHost<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect> {
    override val container =
        container<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect>(
            FavoriteTimeCapsulesState(),
        )

    fun onAction(action: FavoriteTimeCapsulesAction) {
        when (action) {
            is FavoriteTimeCapsulesAction.PullToRefresh -> {
                handlePullToRefresh()
            }

            is FavoriteTimeCapsulesAction.SetSortOrder -> {
                handleSetSortOrder(action.sortOrderLabel)
            }

            is FavoriteTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handlePullToRefresh() =
        intent {
            // todo: call use case

            val timeCapsules =
                (1..15)
                    .toList()
                    .map { it ->
                        TimeCapsuleItemState(
                            id = it.toString(),
                            status = TimeCapsule.STATUS.ARRIVED,
                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                            emotions =
                                listOf(
                                    Emotion(
                                        label = "서운함",
                                        icon = 0,
                                    ),
                                    Emotion(
                                        label = "화남",
                                        icon = 1,
                                    ),
                                    Emotion(
                                        label = "피곤함",
                                        icon = 2,
                                    ),
                                ),
                            isFavorite = true,
                            isFavoriteAt = LocalDateTime.now(),
                            createdAt = LocalDateTime.now(),
                        )
                    }.sortedByDescending {
                        when (state.sortOrder) {
                            SortOrder.SORT_BY_FAVORITE -> it.isFavoriteAt
                            SortOrder.SORT_BY_NEWEST -> it.createdAt
                        }
                    }

            reduce {
                state.copy(timeCapsules = timeCapsules)
            }
        }

    private fun handleSetSortOrder(sortOrderLabel: String) =
        intent {
            try {
                val sortOrder = SortOrder.getByLabel(sortOrderLabel)

                // todo: call use case
                val timeCapsules =
                    (1..15)
                        .toList()
                        .map { it ->
                            TimeCapsuleItemState(
                                id = it.toString(),
                                status = TimeCapsule.STATUS.ARRIVED,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                emotions =
                                    listOf(
                                        Emotion(
                                            label = "서운함",
                                            icon = 0,
                                        ),
                                        Emotion(
                                            label = "화남",
                                            icon = 1,
                                        ),
                                        Emotion(
                                            label = "피곤함",
                                            icon = 2,
                                        ),
                                    ),
                                isFavorite = true,
                                isFavoriteAt = LocalDateTime.now(),
                                createdAt = LocalDateTime.now(),
                            )
                        }.sortedByDescending {
                            when (sortOrder) {
                                SortOrder.SORT_BY_FAVORITE -> it.isFavoriteAt
                                SortOrder.SORT_BY_NEWEST -> it.createdAt
                            }
                        }

                reduce {
                    state.copy(sortOrder = sortOrder, timeCapsules = timeCapsules)
                }
            } catch (e: IllegalArgumentException) {
                Logger.e("Invalid sort order label: $sortOrderLabel")
                return@intent
            } catch (e: Exception) {
                Logger.e(e.toString())
                return@intent
            }
        }

    private fun handleToggleFavorite(id: String) =
        intent {
            val isFavorite = state.timeCapsules.find { it.id == id }?.isFavorite
            if (isFavorite == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }

            // todo: call use case
            postSideEffect(
                ShowToast(
                    if (isFavorite) {
                        ShowToast.FavoriteToast.FAVORITE_REMOVED
                    } else {
                        ShowToast.FavoriteToast.FAVORITE_ADDED
                    },
                ),
            )
            reduce {
                state.copy(
                    timeCapsules =
                        state.timeCapsules.map {
                            if (it.id == id) {
                                it.copy(
                                    isFavorite = !it.isFavorite,
                                )
                            } else {
                                it
                            }
                        },
                )
            }
        }
}
