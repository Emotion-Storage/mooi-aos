package com.emotionstorage.domain.useCase.user

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class GetUserEmailAndNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Pair<String, String>? {
        val userResult = userRepository.getUserSnapshot()

        return if (userResult is DataState.Success) {
            Pair(userResult.data.email, userResult.data.nickname)
        } else {
            null
        }
    }
}
