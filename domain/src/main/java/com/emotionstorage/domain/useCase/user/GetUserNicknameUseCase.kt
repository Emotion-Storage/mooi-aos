package com.emotionstorage.domain.useCase.user

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUserNicknameUseCase
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) {
        suspend operator fun invoke(): Flow<DataState<String>> =
            userRepository.getUser().map {
                when (it) {
                    is DataState.Success -> DataState.Success(it.data.nickname)
                    is DataState.Error -> DataState.Error(it.throwable, it.data)
                    is DataState.Loading -> DataState.Loading(it.isLoading)
                }
            }
    }
