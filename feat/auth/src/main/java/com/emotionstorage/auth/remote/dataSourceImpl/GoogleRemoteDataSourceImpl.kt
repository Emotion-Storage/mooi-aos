package com.emotionstorage.auth.remote.dataSourceImpl

import com.emotionstorage.auth.data.dataSource.GoogleRemoteDataSource
import javax.inject.Inject

class GoogleRemoteDataSourceImpl @Inject constructor () : GoogleRemoteDataSource{
    override suspend fun getIdToken(): String {
        // todo: 구글 소셜 로그인 SDK 연동
        return "google-id-token"
    }
}