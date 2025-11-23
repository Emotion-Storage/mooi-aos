package com.emotionstorage.domain.useCase.user

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.map
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
                it.map {
                    it.nickname
                }
            }
    }
