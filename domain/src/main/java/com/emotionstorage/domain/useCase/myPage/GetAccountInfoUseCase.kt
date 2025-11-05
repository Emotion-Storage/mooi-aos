package com.emotionstorage.domain.useCase.myPage

import com.emotionstorage.domain.repo.MyPageRepository
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() = userRepository.getAccountInfo()
}
