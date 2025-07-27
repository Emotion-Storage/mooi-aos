package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.UserLocalDataSource
import com.emotionstorage.data.modelMapper.UserMapper
import com.emotionstorage.domain.model.User
import com.emotionstorage.domain.repo.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getUser(): User? {
        return userLocalDataSource.getUser()?.run {
            UserMapper.toDomain(this)
        }
    }
}