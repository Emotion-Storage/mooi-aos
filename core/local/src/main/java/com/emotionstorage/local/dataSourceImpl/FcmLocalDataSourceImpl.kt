package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.FcmLocalDataSource
import javax.inject.Inject

class FcmLocalDataSourceImpl @Inject constructor(): FcmLocalDataSource{
    override suspend fun saveToken(token: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getToken(): String? {
        TODO("Not yet implemented")
    }

}
