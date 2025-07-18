package com.emotionstorage.domain.useCase

import com.emotionstorage.domain.model.User

interface GetUserUseCase {
    suspend operator fun invoke(): User
}