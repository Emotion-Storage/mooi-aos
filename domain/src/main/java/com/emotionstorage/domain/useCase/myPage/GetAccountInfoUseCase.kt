package com.emotionstorage.domain.useCase.myPage

import com.emotionstorage.domain.repo.MyPageRepository
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository,
) {
    suspend operator fun invoke() = myPageRepository.getAccountInfo()
}
