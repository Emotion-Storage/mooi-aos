package com.emotionstorage.domain.useCase

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Flow<DataState<User>> =
        userRepository.getUser()
}
