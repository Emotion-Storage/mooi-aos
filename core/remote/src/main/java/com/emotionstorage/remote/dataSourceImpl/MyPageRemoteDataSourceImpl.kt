package com.emotionstorage.remote.datasourceImpl

import com.emotionstorage.data.dataSource.MyPageRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.MyPage
import com.emotionstorage.remote.api.MyPageApiService
import com.emotionstorage.remote.response.myPage.toDomainModel
import com.emotionstorage.remote.response.toDataState
import javax.inject.Inject

class MyPageRemoteDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) : MyPageRemoteDataSource {
    override suspend fun getMyPageOverview(): DataState<MyPage> {
        return myPageApiService.getMyPageOverview().toDataState { dto ->
            dto.toDomainModel()
        }
    }
}
