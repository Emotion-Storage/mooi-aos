package com.emotionstorage.data.model

data class FavoriteTimeCapsuleRemoteKeyEntity(
    val id: Long,
    val queryKey: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long,
) {
    companion object {
        fun generateQueryKey(
            sortBy: String
        ) = "favorite-$sortBy"
    }
}
