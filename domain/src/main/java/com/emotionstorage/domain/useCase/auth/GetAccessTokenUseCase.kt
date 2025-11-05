package com.emotionstorage.domain.useCase.auth

import com.emotionstorage.domain.repo.SessionRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val sessionRepository: SessionRepository,
) {
    suspend operator fun invoke(): String? = sessionRepository.getSession()?.accessToken
}
