package com.emotionstorage.data.model

data class NotificationEntity(
    val id: Long,
    val title: String,
    val body: String,
    val isRead: Boolean,
    val type: String,
    val targetId: String? = null,
    val pageData: PageData? = null,
) {
    data class PageData(
        val page: Int,
        val hasNextPage: Boolean,
    )
}
