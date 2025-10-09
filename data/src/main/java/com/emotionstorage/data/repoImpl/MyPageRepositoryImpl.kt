package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.MyPageRemoteDataSource
import com.emotionstorage.data.dataSource.UserRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AccountInfo
import com.emotionstorage.domain.model.MyPage
import com.emotionstorage.domain.repo.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(
    private val myPageRemoteDataSource: MyPageRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource,
) : MyPageRepository {
    override suspend fun getMyPageOverview(): Flow<DataState<MyPage>> =
        flow {
            emit(myPageRemoteDataSource.getMyPageOverview())
        }

    override suspend fun getAccountInfo(): Flow<DataState<AccountInfo>> =
        flow {
            emit(userRemoteDataSource.getUserAccountInfo())
        }
}
