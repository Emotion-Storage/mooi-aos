package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.domain.model.User

internal object UserMapper {
    fun mapToDomain(entity: UserEntity): User {
        return User(
            id = entity.id,
            socialType = when (entity.socialType) {
                UserEntity.AuthProvider.GOOGLE -> User.AuthProvider.GOOGLE
                UserEntity.AuthProvider.KAKAO -> User.AuthProvider.KAKAO
            },
            socialId = entity.socialId,
            email = entity.email,
            name = entity.name,
            profileImageUrl = entity.profileImageUrl,
            accessToken = entity.accessToken,
            refreshToken = entity.refreshToken,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun mapToData(domain: User): UserEntity {
        return UserEntity(
            id = domain.id,
            socialType = when (domain.socialType) {
                User.AuthProvider.GOOGLE -> UserEntity.AuthProvider.GOOGLE
                User.AuthProvider.KAKAO -> UserEntity.AuthProvider.KAKAO
            },
            socialId = domain.socialId,
            email = domain.email,
            name = domain.name,
            profileImageUrl = domain.profileImageUrl,
            accessToken = domain.accessToken,
            refreshToken = domain.refreshToken,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }
}