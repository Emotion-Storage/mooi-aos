package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.UserLocalDataSource
import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.data.modelMapper.UserMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun saveUser(user: User): Boolean {
        return userLocalDataSource.saveUser(UserMapper.toData(user))
    }

    override suspend fun getUser(): Flow<DataState<User>> = flow {
        emit(DataState.Loading(isLoading = true))
        try {
            val user = (userLocalDataSource.getUser() ?: {
                // todo: get user from remote if null
                // userRemoteDataSource.getUser()
                null
            }) as UserEntity?

            if(user != null){
                emit(DataState.Success(UserMapper.toDomain(user!!)))
            }else{
                emit(DataState.Error(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(DataState.Error(e))
        } finally {
            emit(DataState.Loading(isLoading = false))
        }
    }

    override suspend fun deleteUser(): Boolean {
        return userLocalDataSource.deleteUser()
    }
}