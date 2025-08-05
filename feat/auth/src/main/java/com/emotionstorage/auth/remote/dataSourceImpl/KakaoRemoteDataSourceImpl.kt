package com.emotionstorage.auth.remote.dataSourceImpl

import com.emotionstorage.auth.data.dataSource.KakaoRemoteDataSource
import com.emotionstorage.common.DataResource
import javax.inject.Inject

class KakaoRemoteDataSourceImpl @Inject constructor(): KakaoRemoteDataSource{
    override suspend fun getIdToken(): DataResource<String> {
        // todo: 카카오 소셜 로그인 SDK 연동
        return DataResource.success("kakao-id-token")
    }
}