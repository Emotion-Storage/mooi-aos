package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.ReissueRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.remote.api.ReissueApiService
import com.emotionstorage.remote.response.CustomHttpException
import com.orhanobut.logger.Logger
import java.io.IOException
import javax.inject.Inject

class ReissueRemoteDataSourceImpl @Inject constructor(
    private val reissueApiService: ReissueApiService,
) : ReissueRemoteDataSource {
    override suspend fun reissueAccessToken(): DataState<String> =
        try {
            reissueApiService.postReissue().data?.accessToken?.run {
                DataState.Success(this)
            } ?: DataState.Error(
                Throwable("No access token received"),
            )
        } catch (e: IOException) {
            if (e !is CustomHttpException) {
                Logger.e("Reissue network exception, $e")
                DataState.Error(e, ErrorCode.NETWORK_ERROR)
            } else {
                Logger.e("Reissue http exception, $e")
                DataState.Error(e, ErrorCode.toErrorCode(e.code ?: ""))
            }
        } catch (e: Exception) {
            Logger.e("Reissue api failed", e)
            DataState.Error(e)
        }
}
