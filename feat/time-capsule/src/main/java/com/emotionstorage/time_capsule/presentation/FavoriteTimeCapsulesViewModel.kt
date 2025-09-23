package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleState
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: SortOrder = SortOrder.SORT_BY_NEWEST,
    val timeCapsules: List<TimeCapsuleState> = emptyList()
) {
    enum class SortOrder(val label: String) {
        SORT_BY_NEWEST("최신 날짜순"),
        SORT_BY_FAVORITE("즐겨찾기순")
    }
}

sealed class FavoriteTimeCapsulesAction {
    object PullToRefresh : FavoriteTimeCapsulesAction()
    data class SetSortOrder(val sortOrder: FavoriteTimeCapsulesState.SortOrder) :
        FavoriteTimeCapsulesAction()

    data class ToggleFavorite(val id: String) : FavoriteTimeCapsulesAction()
}

sealed class FavoriteTimeCapsulesSideEffect {
    data class ShowToast(val message: String, val showCheckIcon: Boolean = false) :
        FavoriteTimeCapsulesSideEffect()
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    // private val getFavoriteTimeCapsules: GetFavoriteTimeCapsulesUseCase,
    // private val toggleFavoriteTimeCapsule: ToggleFavoriteTimeCapsuleUseCase
) : ViewModel(), ContainerHost<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect> {
    override val container = container<FavoriteTimeCapsulesState, FavoriteTimeCapsulesSideEffect>(
        FavoriteTimeCapsulesState()
    )

    fun onAction(action: FavoriteTimeCapsulesAction) {
        when (action) {
            is FavoriteTimeCapsulesAction.PullToRefresh -> {
                handlePullToRefresh()
            }

            is FavoriteTimeCapsulesAction.SetSortOrder -> {
                handleSetSortOrder(action.sortOrder)
            }

            is FavoriteTimeCapsulesAction.ToggleFavorite -> {
                handleToggleFavorite(action.id)
            }
        }
    }

    private fun handlePullToRefresh() = intent {
        // todo: call use case

        val timeCapsules = (1..15).toList().map { it ->
            TimeCapsuleState(
                id = it.toString(),
                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                emotions = listOf(
                    Emotion(
                        label = "서운함",
                        icon = 0,
                    ), Emotion(
                        label = "화남",
                        icon = 1,
                    ), Emotion(
                        label = "피곤함",
                        icon = 2,
                    )
                ),
                isFavorite = true,
                isFavoriteAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now()
            )
        }.sortedByDescending {
            when (state.sortOrder) {
                FavoriteTimeCapsulesState.SortOrder.SORT_BY_FAVORITE -> it.isFavoriteAt
                FavoriteTimeCapsulesState.SortOrder.SORT_BY_NEWEST -> it.createdAt
            }
        }

        reduce {
            state.copy(timeCapsules = timeCapsules)
        }

    }

    private fun handleSetSortOrder(sortOrder: FavoriteTimeCapsulesState.SortOrder) = intent {
        // todo: call use case

        val timeCapsules = (1..15).toList().map { it ->
            TimeCapsuleState(
                id = it.toString(),
                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                emotions = listOf(
                    Emotion(
                        label = "서운함",
                        icon = 0,
                    ), Emotion(
                        label = "화남",
                        icon = 1,
                    ), Emotion(
                        label = "피곤함",
                        icon = 2,
                    )
                ),
                isFavorite = true,
                isFavoriteAt = LocalDateTime.now(),
                createdAt = LocalDateTime.now()
            )
        }.sortedByDescending {
            when (sortOrder) {
                FavoriteTimeCapsulesState.SortOrder.SORT_BY_FAVORITE -> it.isFavoriteAt
                FavoriteTimeCapsulesState.SortOrder.SORT_BY_NEWEST -> it.createdAt
            }
        }

        reduce {
            state.copy(sortOrder = sortOrder, timeCapsules = timeCapsules)
        }
    }

    private fun handleToggleFavorite(id: String) = intent {
        val isFavorite = state.timeCapsules.find { it.id == id }?.isFavorite
        if (isFavorite == null) {
            Logger.e("Cannot find time capsule of id ${id}")
            return@intent
        }

        // todo: call use case

        postSideEffect(
            FavoriteTimeCapsulesSideEffect.ShowToast(
                message = if (isFavorite) "즐겨찾기가 해제되었습니다." else "즐겨찾기에 추가되었습니다.",
                showCheckIcon = true
            )
        )
        reduce {
            state.copy(
                timeCapsules = state.timeCapsules.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
            )
        }
    }
}