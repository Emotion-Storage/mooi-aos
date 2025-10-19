package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.SessionRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): String? =
        sessionRepository.getSession()?.accessToken
}
