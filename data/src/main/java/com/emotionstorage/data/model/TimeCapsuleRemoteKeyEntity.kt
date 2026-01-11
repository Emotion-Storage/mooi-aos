package com.emotionstorage.data.model

import java.time.LocalDateTime

data class TimeCapsuleRemoteKeyEntity(
    val id: Long,
    val queryKey: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long,
) {
    companion object {
        fun generateQueryKey(
            status: String,
            startDate: LocalDateTime,
            endDate: LocalDateTime,
        ) =
            "$status-$startDate-$endDate"
    }
}

