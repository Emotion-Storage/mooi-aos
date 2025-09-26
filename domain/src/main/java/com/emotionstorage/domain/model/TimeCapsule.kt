package com.emotionstorage.domain.model

import java.time.LocalDateTime

data class TimeCapsule(
    val id: String,
    val status: STATUS,
    val isFavorite: Boolean = false,
    val title: String = "",
    val summary: String = "",
    val emotions: List<Emotion> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String? = null,
    val logs: List<OpenLog> = emptyList(),
    // 생성 시각
    val createdAt: LocalDateTime,
    // 도착 시각
    val arriveAt: LocalDateTime? = null,
    // 즐겨찾기 설정 시각
    val favoriteAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    enum class STATUS {
        // 임시저장 (열람일 지정 X)
        TEMPORARY,

        // 잠김 (열람일 이전)
        LOCKED,

        // 도착 (열람일 이후, 열람 전)
        ARRIVED,

        // 열림 (열람일 이후, 열람 완료)
        OPENED,
    }

    data class Emotion(
        val label: String,
        // todo: text로 이모지 관리하기
        val icon: Int,
        val percentage: Float? = null,
    )

    data class OpenLog(
        // 열람 시각
        val openedAt: LocalDateTime,
        // 최초 열람 여부
        val isFirst: Boolean,
    )
}
