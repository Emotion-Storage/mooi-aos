package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.local.FcmLocalDataSource
import com.emotionstorage.data.dataSource.remote.FcmRemoteDataSource
import com.emotionstorage.domain.repo.FcmRepository
import io.github.aakira.napier.Napier
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val localDataSource: FcmLocalDataSource, private val remoteDatasource: FcmRemoteDataSource
) : FcmRepository {
    override suspend fun registerToken(token: String): Boolean {
        try {
            localDataSource.saveToken(token)
            remoteDatasource.registerToken(token)
            return true
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return false
        }
    }

    override suspend fun saveToken(token: String): Boolean {
        try {
            return localDataSource.saveToken(token)
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return false
        }
    }

    override suspend fun getToken(): String? {
        try {
            return localDataSource.getToken()
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return null
        }
    }

    override suspend fun deleteToken(): Boolean {
        try {
            localDataSource.getToken()?.let {
                remoteDatasource.deleteToken(it)
            }
            return true
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return false
        }
    }
}
