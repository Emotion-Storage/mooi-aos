package com.emotionstorage.data.repoImpl

import com.emotionstorage.domain.repo.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
) : FcmRepository {
    override suspend fun registerToken(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun saveToken(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteToken(): Boolean {
        TODO("Not yet implemented")
    }
}
