package com.emotionstorage.domain.useCase.key

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetKeyCountUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): DataState<Int> = userRepository.getKeyCount()
}
