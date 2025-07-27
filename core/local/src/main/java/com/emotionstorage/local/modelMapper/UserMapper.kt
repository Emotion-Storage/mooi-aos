package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.local.model.UserLocal

internal object UserMapper {
    fun toLocal(entity: UserEntity): UserLocal {
        return UserLocal(
            id = entity.id,
            socialType = entity.socialType.name,
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

    fun toEntity(local: UserLocal): UserEntity {
        return UserEntity(
            id = local.id,
            socialType = when (local.socialType) {
                "GOOGLE" -> UserEntity.AuthProvider.GOOGLE
                "KAKAO" -> UserEntity.AuthProvider.KAKAO
                else -> throw IllegalArgumentException("Invalid social type: ${local.socialType}")
            },
            socialId = local.socialId,
            email = local.email,
            name = local.name,
            profileImageUrl = local.profileImageUrl,
            accessToken = local.accessToken,
            refreshToken = local.refreshToken,
            createdAt = local.createdAt,
            updatedAt = local.updatedAt,
        )
    }
}