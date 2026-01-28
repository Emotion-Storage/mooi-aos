package com.emotionstorage.time_capsule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.useCase.timeCapsule.GetPagedFavoriteTimeCapsulesUseCase
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseViewModel
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.modelMapper.TimeCapsuleMapper
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class FavoriteTimeCapsulesState(
    val sortOrder: FavoriteSortBy = FavoriteSortBy.NEWEST,
    val timeCapsulesFlow: Flow<PagingData<TimeCapsuleItemState>>? = null,
)

sealed class FavoriteTimeCapsulesAction {
    object Init : FavoriteTimeCapsulesAction()

    data class SetSortOrder(
        val sortOrderLabel: String,
    ) : FavoriteTimeCapsulesAction()
}

@HiltViewModel
class FavoriteTimeCapsulesViewModel @Inject constructor(
    private val getFavoriteTimeCapsules: GetPagedFavoriteTimeCapsulesUseCase,
) : BaseViewModel<FavoriteTimeCapsulesState>(FavoriteTimeCapsulesState()) {

    fun onAction(action: FavoriteTimeCapsulesAction) {
        when (action) {
            is FavoriteTimeCapsulesAction.Init -> {
                handleInit()
            }

            is FavoriteTimeCapsulesAction.SetSortOrder -> {
                handleSetSortOrder(action.sortOrderLabel)
            }
        }
    }

    private fun handleInit() = setSortOrder(FavoriteSortBy.NEWEST)

    private fun handleSetSortOrder(sortOrderLabel: String) = setSortOrder(FavoriteSortBy.getByLabel(sortOrderLabel))

    private fun setSortOrder(sortOrder: FavoriteSortBy) =
        baseIntent {
            try {
                reduce {
                    state.copy(
                        sortOrder = sortOrder,
                        timeCapsulesFlow =
                            getFavoriteTimeCapsules(sortOrder)
                                .cachedIn(viewModelScope)
                                .map { pagingData ->
                                    pagingData.map {
                                        TimeCapsuleMapper.toUi(it)
                                    }
                                },
                    )
                }
            } catch (e: Exception) {
                Logger.e("Failed to get favorite time capsules, $e")
                throw BaseException(
                    message = "Failed to get favorite time capsules",
                    code = ErrorCode.UNKNOWN,
                    cause = e,
                )
            }
        }
}
