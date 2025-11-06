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
            Napier.d("save token & register token to server - $token")
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
            Napier.d("save token - $token")
            return localDataSource.saveToken(token)
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return false
        }
    }

    override suspend fun getToken(): String? {
        try {
            Napier.d("get token")
            return localDataSource.getToken()
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return null
        }
    }

    override suspend fun deleteToken(): Boolean {
        try {
            localDataSource.getToken()?.let {
                Napier.d("delete token from server - $it")
                remoteDatasource.deleteToken(it)
            }
            return true
        } catch (e: Exception) {
            Napier.e(e.message.toString())
            return false
        }
    }
}
