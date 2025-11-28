package com.emotionstorage.local.dataSourceImpl

import com.emotionstorage.data.dataSource.local.UserLocalDataSource
import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.local.modelMapper.UserMapper
import com.emotionstorage.local.room.dao.UserDao
import com.orhanobut.logger.Logger
import javax.inject.Inject

class UserLocalDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
) : UserLocalDataSource {
    override suspend fun saveUser(user: UserEntity): Boolean {
        try {
            userDao.insertUser(UserMapper.toLocal(user))
            return true
        } catch (e: Exception) {
            Logger.e("saveUser error: $e")
            return false
        }
    }

    override suspend fun getUser(): UserEntity? {
        try {
            return userDao.getUser()?.run {
                UserMapper.toEntity(this)
            }
        } catch (e: Exception) {
            Logger.e("getUser error: $e")
            throw e
        }
    }

    override suspend fun deleteUser(): Boolean {
        try {
            userDao.deleteUser()
            return true
        } catch (e: Exception) {
            Logger.e("deleteUser error: $e")
            return false
        }
    }

    override suspend fun updateUserNickname(nickname: String) {
        try {
            userDao.updateUserNickname(nickname)
        } catch (e: Exception) {
            Logger.e("updateUserNickname error: $e")
        }
    }
}
