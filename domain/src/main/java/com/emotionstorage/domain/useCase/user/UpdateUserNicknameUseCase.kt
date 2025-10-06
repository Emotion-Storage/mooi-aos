package com.emotionstorage.domain.useCase.user

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class UpdateUserNicknameUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(nickname: String): DataState<Unit?> {
        return userRepository.updateUserNickname(nickname)
    }
}
