package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ReissueRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.remote.api.ReissueApiService
import com.orhanobut.logger.Logger
import javax.inject.Inject

class ReissueRemoteDataSourceImpl @Inject constructor(
    private val reissueApiService: ReissueApiService,
): ReissueRemoteDataSource {
    override suspend fun reissueAccessToken(): DataState<String> = try{
        reissueApiService.postReissue().data?.accessToken?.run{
            DataState.Success(this)
        }?: DataState.Error(
            Throwable("No access token received")
        )
    }catch(e: Exception){
        Logger.e("Reissue api failed", e)
        DataState.Error(e)
    }
}
