package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.local.model.UserLocal

internal object UserMapper {
    fun toLocal(entity: UserEntity): UserLocal =
        UserLocal(
            id = entity.id,
            socialType = entity.socialType.name,
            socialId = entity.socialId,
            email = entity.email,
            name = entity.name,
            profileImageUrl = entity.profileImageUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    fun toEntity(local: UserLocal): UserEntity =
        UserEntity(
            id = local.id,
            socialType =
                when (local.socialType) {
                    "GOOGLE" -> UserEntity.AuthProvider.GOOGLE
                    "KAKAO" -> UserEntity.AuthProvider.KAKAO
                    else -> throw IllegalArgumentException("Invalid social type: ${local.socialType}")
                },
            socialId = local.socialId,
            email = local.email,
            name = local.name,
            profileImageUrl = local.profileImageUrl,
            createdAt = local.createdAt,
            updatedAt = local.updatedAt,
        )
}
