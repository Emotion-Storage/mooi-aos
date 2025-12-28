package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.local.UserLocalDataSource
import com.emotionstorage.data.dataSource.remote.UserRemoteDataSource
import com.emotionstorage.data.modelMapper.UserMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.common.map
import com.emotionstorage.domain.model.AccountInfo
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.model.toUser
import com.emotionstorage.domain.repo.UserRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val localDataSource: UserLocalDataSource,
        private val remoteDataSource: UserRemoteDataSource,
    ) : UserRepository {
        override suspend fun saveUser(user: User): Boolean = localDataSource.saveUser(UserMapper.toData(user))

        override suspend fun getUserSnapshot(): DataState<User> =
            try {
                // return user from local data source first
                val localUser = localDataSource.getUser()
                if (localUser != null) {
                    DataState.Success(UserMapper.toDomain(localUser))
                } else {
                    // fetch user from remote
                    remoteDataSource
                        .getUserAccountInfo()
                        .map {
                            it.toUser()
                        }.also {
                            if (it is DataState.Success) {
                                // save to local if successful
                                try {
                                    val isSaveSuccess = localDataSource.saveUser(UserMapper.toData(it.data))
                                    if (!isSaveSuccess) throw Exception("save user failed")
                                } catch (e: Exception) {
                                    Napier.e("save user failed", e)
                                }
                            }
                        }
                }
            } catch (e: Exception) {
                DataState.Error(e)
            }

        override suspend fun getUser(): Flow<DataState<User>> =
            flow {
                emit(DataState.Loading(isLoading = true))
                try {
                    // return user from local data source first
                    val localUser = localDataSource.getUser()
                    if (localUser != null) {
                        emit(DataState.Success(UserMapper.toDomain(localUser)))
                    } else {
                        // fetch user from remote
                        remoteDataSource.getUserAccountInfo().handle(
                            onSuccess = {
                                // save to local if successful
                                try {
                                    val isSaveSuccess = localDataSource.saveUser(UserMapper.toData(it.toUser()))
                                    if (!isSaveSuccess) throw Exception("save user failed")
                                } catch (e: Exception) {
                                    Napier.e("save user failed", e)
                                }
                                // return success with remote user data, regardless of save success/failure
                                emit(DataState.Success(it.toUser()))
                            },
                            onError = { throwable, code, message ->
                                emit(DataState.Error(throwable, code, message))
                            },
                        )
                    }
                } catch (e: Exception) {
                    emit(DataState.Error(e))
                } finally {
                    emit(DataState.Loading(isLoading = false))
                }
            }

        override suspend fun getAndSaveUser(): Boolean =
            try {
                val result = remoteDataSource.getUserAccountInfo()
                if (result is DataState.Success) {
                    localDataSource.saveUser(UserMapper.toData(result.data.toUser()))
                } else {
                    Napier.e("getUser error in getAndSaveUser", (result as DataState.Error).throwable)
                    false
                }
            } catch (e: Exception) {
                Napier.e("getAndSaveUser error", e)
                false
            }

        override suspend fun deleteUser(): Boolean = localDataSource.deleteUser()

        override suspend fun updateUserNickname(nickname: String) =
            remoteDataSource
                .updateUserNickname(nickname)
                .also { state ->
                    if (state is DataState.Success) {
                        val localUpdateSuccess = localDataSource.updateUserNickname(nickname)
                        if (!localUpdateSuccess) {
                            // delete user on update error - fetch user on next get user call
                            deleteUser()
                        }
                    }
                }

        override suspend fun getKeyCount(): DataState<Int> = remoteDataSource.getKeyCount()

        override suspend fun getAccountInfo(): DataState<AccountInfo> = remoteDataSource.getUserAccountInfo()
    }
