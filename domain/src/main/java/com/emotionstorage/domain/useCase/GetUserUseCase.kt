package com.emotionstorage.domain.useCase

import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): User? {
        return userRepository.getUser()
    }
}