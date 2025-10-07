package com.emotionstorage.remote.datasourceImpl

import com.emotionstorage.data.dataSource.UserRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AccountInfo
import com.emotionstorage.remote.api.UserApiService
import com.emotionstorage.remote.request.myPage.UpdateNicknameParam
import com.emotionstorage.remote.request.myPage.toRequestBody
import com.emotionstorage.remote.response.myPage.toDomain
import com.emotionstorage.remote.response.toDataState
import com.emotionstorage.remote.response.toEmptyDataState
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService,
) : UserRemoteDataSource {
    override suspend fun updateUserNickname(nickname: String): DataState<Unit> =
        try {
            val dto = userApiService.updateNickName(UpdateNicknameParam(nickname).toRequestBody())
            dto.toEmptyDataState()
        } catch (e: Exception) {
            DataState.Error(e)
        }

    override suspend fun getUserAccountInfo(): DataState<AccountInfo> =
        try {
            userApiService.getAccountInfo().toDataState { dto ->
                dto.toDomain()
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }
}
