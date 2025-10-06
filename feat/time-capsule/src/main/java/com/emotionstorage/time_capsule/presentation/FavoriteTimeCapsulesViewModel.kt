package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.common.collectDataState
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase
import com.emotionstorage.domain.useCase.timeCapsule.ToggleFavoriteUseCase.ToggleToastResult
import com.emotionstorage.time_capsule.presentation.FavoriteTimeCapsulesSideEffect.ShowToast
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: FavoriteSortBy = FavoriteSortBy.NEWEST,
    val timeCapsules: List<TimeCapsuleItemState> = emptyList(),
)

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
    private val toggleFavorite: ToggleFavoriteUseCase,
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
                            status = TimeCapsule.STATUS.OPENED,
                            title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                            emotions =
                                listOf(
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE14",
                                        label = "서운함",
                                        percentage = 30.0f
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83D\uDE0A",
                                        label = "고마움",
                                        percentage = 30.0f
                                    ),
                                    TimeCapsule.Emotion(
                                        emoji = "\uD83E\uDD70",
                                        label = "안정감",
                                        percentage = 80.0f
                                    ),
                                ),
                            isFavorite = true,
                            isFavoriteAt = LocalDateTime.now(),
                            createdAt = LocalDateTime.now(),
                            expireAt = LocalDateTime.now().plusHours(5),
                        )
                    }.sortedByDescending {
                        when (state.sortOrder) {
                            FavoriteSortBy.FAVORITE_AT -> it.isFavoriteAt
                            FavoriteSortBy.NEWEST -> it.createdAt
                        }
                    }

            reduce {
                state.copy(timeCapsules = timeCapsules)
            }
        }

    private fun handleSetSortOrder(sortOrderLabel: String) =
        intent {
            try {
                val sortOrder = FavoriteSortBy.getByLabel(sortOrderLabel)

                // todo: call use case
                val timeCapsules =
                    (1..15)
                        .toList()
                        .map { it ->
                            TimeCapsuleItemState(
                                id = it.toString(),
                                status = TimeCapsule.STATUS.OPENED,
                                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                                emotions =
                                    listOf(
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83D\uDE14",
                                            label = "서운함",
                                            percentage = 30.0f
                                        ),
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83D\uDE0A",
                                            label = "고마움",
                                            percentage = 30.0f
                                        ),
                                        TimeCapsule.Emotion(
                                            emoji = "\uD83E\uDD70",
                                            label = "안정감",
                                            percentage = 80.0f
                                        ),
                                    ),
                                isFavorite = true,
                                isFavoriteAt = LocalDateTime.now(),
                                createdAt = LocalDateTime.now(),
                                expireAt = LocalDateTime.now().plusHours(5),
                            )
                        }.sortedByDescending {
                            when (sortOrder) {
                                FavoriteSortBy.FAVORITE_AT -> it.isFavoriteAt
                                FavoriteSortBy.NEWEST -> it.createdAt
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
            if (state.timeCapsules.find { it.id == id } == null) {
                Logger.e("Cannot find time capsule of id $id")
                return@intent
            }

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
                    flow = toggleFavorite(id),
                    onSuccess = {
                        if (it == ToggleToastResult.FAVORITE_ADDED) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_ADDED))
                            updateFavorite(id, true)
                        } else if (it == ToggleToastResult.FAVORITE_REMOVED) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_REMOVED))
                            updateFavorite(id, false)
                        }
                    },
                    onError = { throwable, data ->
                        if (data == ToggleToastResult.FAVORITE_FULL) {
                            postSideEffect(ShowToast(ShowToast.FavoriteToast.FAVORITE_FULL))
                        }
                    },
                )
            }
        }
}
