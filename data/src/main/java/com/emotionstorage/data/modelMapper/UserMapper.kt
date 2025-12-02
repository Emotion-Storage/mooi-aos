package com.emotionstorage.data.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.domain.model.User

internal object UserMapper {
    fun toDomain(entity: UserEntity): User =
        User(
            socialType =
                when (entity.socialType) {
                    UserEntity.AuthProvider.GOOGLE -> User.AuthProvider.GOOGLE
                    UserEntity.AuthProvider.KAKAO -> User.AuthProvider.KAKAO
                },
            email = entity.email,
            nickname = entity.name,
            profileImageUrl = entity.profileImageUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    fun toData(domain: User): UserEntity =
        UserEntity(
            socialType =
                when (domain.socialType) {
                    User.AuthProvider.GOOGLE -> UserEntity.AuthProvider.GOOGLE
                    User.AuthProvider.KAKAO -> UserEntity.AuthProvider.KAKAO
                },
            email = domain.email,
            name = domain.nickname,
            profileImageUrl = domain.profileImageUrl,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
}
