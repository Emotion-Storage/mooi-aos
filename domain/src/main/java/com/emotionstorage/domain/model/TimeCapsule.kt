package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class TimeCapsule(
    val id: String,
    val status: STATUS,
    val isFavorite: Boolean = false,
    val title: String,
    val summary: String,
    val emotions: List<Emotion> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String? = null,
    val logs: List<OpenLog> = emptyList(),
    val createdAt: LocalDateTime, // 생성일
    val openAt: LocalDateTime? = null, // 열람일
    val updatedAt: LocalDateTime,
) {
    enum class STATUS {
        TEMPORARY, // 임시저장 (열람일 지정 X)
        LOCKED, // 잠김 (열람일 이전)
        ARRIVED, // 도착 (열람일 이후, 열람 전)
        OPENED, // 열림 (열람일 이후, 열람 완료)
    }

    data class Emotion(
        val label: String,
        val percentage: Float,
    )

    data class OpenLog(
        val openedAt: LocalDateTime, // 열람 시각
        val isFirst: Boolean, // 최초 열람 여부
    )
}