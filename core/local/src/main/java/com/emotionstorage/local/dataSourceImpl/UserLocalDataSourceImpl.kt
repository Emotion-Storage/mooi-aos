package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.UserLocalDataSource
import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.local.modelMapper.UserMapper
import com.emotionstorage.local.room.dao.UserDao
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
) : UserLocalDataSource {
    override suspend fun saveUser(user: UserEntity): Boolean {
        try {
            userDao.insertUser(UserMapper.toLocal(user))
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override suspend fun getUser(): UserEntity? {
        try {
            return userDao.getUser()?.run {
                UserMapper.toEntity(this)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun deleteUser(): Boolean {
        try {
            userDao.deleteUser()
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
