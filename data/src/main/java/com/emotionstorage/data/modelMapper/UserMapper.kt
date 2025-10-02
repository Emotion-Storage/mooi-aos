package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.domain.model.User

internal object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(
            id = entity.id,
            socialType =
                when (entity.socialType) {
                    UserEntity.AuthProvider.GOOGLE -> User.AuthProvider.GOOGLE
                    UserEntity.AuthProvider.KAKAO -> User.AuthProvider.KAKAO
                },
            socialId = entity.socialId,
            email = entity.email,
            nickname = entity.name,
            profileImageUrl = entity.profileImageUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    fun toData(domain: User): UserEntity =
        UserEntity(
            id = domain.id,
            socialType =
                when (domain.socialType) {
                    User.AuthProvider.GOOGLE -> UserEntity.AuthProvider.GOOGLE
                    User.AuthProvider.KAKAO -> UserEntity.AuthProvider.KAKAO
                },
            socialId = domain.socialId,
            email = domain.email,
            name = domain.nickname,
            profileImageUrl = domain.profileImageUrl,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
}
