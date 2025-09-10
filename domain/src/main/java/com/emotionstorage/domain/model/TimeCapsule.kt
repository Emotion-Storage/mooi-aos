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
    val createdAt: LocalDateTime, // 생성 시각
    val arriveAt: LocalDateTime? = null, // 도착 시각
    val favoriteAt: LocalDateTime? = null, // 즐겨찾기 설정 시각
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
        val icon: Int, // todo: 감정 고정된 목록으로 관리할 것인지 BE/AI와 논의 <- 이모지 매핑 로직 고민
        val percentage: Float? = null,
    )

    data class OpenLog(
        val openedAt: LocalDateTime, // 열람 시각
        val isFirst: Boolean, // 최초 열람 여부
    )
}