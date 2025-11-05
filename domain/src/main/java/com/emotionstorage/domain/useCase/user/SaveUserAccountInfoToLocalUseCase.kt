package com.emotionstorage.domain.useCase.user

import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class SaveUserAccountInfoToLocalUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User) = userRepository.saveUser(user)
}
