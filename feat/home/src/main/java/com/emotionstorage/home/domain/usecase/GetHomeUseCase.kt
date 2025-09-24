package com.emotionstorage.home.domain.usecase

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.home.domain.model.Home
import com.emotionstorage.home.domain.repo.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Flow<DataState<Home>> = homeRepository.getHome()
}
