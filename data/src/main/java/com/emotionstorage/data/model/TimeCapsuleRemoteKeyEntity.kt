package com.emotionstorage.data.model

import java.time.LocalDate

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
            startDate: LocalDate,
            endDate: LocalDate,
        ) =
            "$status-$startDate-$endDate"
    }
}

