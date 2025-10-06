package com.emotionstorage.remote.request.timeCapsule

import kotlinx.serialization.Serializable

@Serializable
data class PatchTimeCapsuleFavoriteRequest (
    val addFavorite: Boolean
)
