package com.emotionstorage.data.model

import java.time.LocalDateTime

data class NotificationEntity(
    val id: Long,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val arrivedAt: LocalDateTime,
    val type: String,
    val targetId: Long? = null,
    val pageData: PageData? = null,
) {
    data class PageData(
        val page: Int,
        val hasNextPage: Boolean,
    )
}
