package com.emotionstorage.domain.useCase.myPage

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.MyPage
import com.emotionstorage.domain.repo.MyPageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMyPageOverviewUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke(): Flow<DataState<MyPage>> = myPageRepository.getMyPageOverview()
}
