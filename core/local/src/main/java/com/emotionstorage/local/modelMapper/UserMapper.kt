package com.emotionstorage.local.modelMapper

import com.emotionstorage.data.model.UserEntity
import com.emotionstorage.local.model.UserLocal

internal object UserMapper {
    fun toLocal(entity: UserEntity): UserLocal =
        UserLocal(
            socialType = entity.socialType.name,
            // todo: delete social id from db
            socialId = "",
            email = entity.email,
            name = entity.name,
            profileImageUrl = entity.profileImageUrl,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )

    fun toEntity(local: UserLocal): UserEntity =
        UserEntity(
            socialType =
                when (local.socialType) {
                    "GOOGLE" -> UserEntity.AuthProvider.GOOGLE
                    "KAKAO" -> UserEntity.AuthProvider.KAKAO
                    else -> throw IllegalArgumentException("Invalid social type: ${local.socialType}")
                },
            email = local.email,
            name = local.name,
            profileImageUrl = local.profileImageUrl,
            createdAt = local.createdAt,
            updatedAt = local.updatedAt,
        )
}
