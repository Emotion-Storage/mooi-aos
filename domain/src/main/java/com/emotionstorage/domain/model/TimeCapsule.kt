package com.emotionstorage.domain.model

import java.time.LocalDateTime

const val TIME_CAPSULE_TEMPORARY_HOURS = 24

data class TimeCapsule(
    val id: Long,
    val status: Status,
    val title: String,
    val summary: String,
    val isFavorite: Boolean = false,
    val emotions: List<Emotion> = emptyList(),
    val comments: List<String> = emptyList(),
    val note: String = "",
    // 타임캡슐 소스 대화 시작 시각
    val historyDate: LocalDateTime,
    // 타임캡슐 생성/갱신 시각
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    // 타임캡슐 오픈 시각
    val openAt: LocalDateTime? = null,
    // 즐겨찾기 설정 시각
    val favoriteAt: LocalDateTime? = null,
) {
    // 임시저장 만료 시각
    val expireAt: LocalDateTime? =
        if (status == Status.TEMPORARY)
            createdAt.plusHours(TIME_CAPSULE_TEMPORARY_HOURS.toLong())
        else null

    enum class Status {
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
        val emoji: String,
        val label: String,
        val percentage: Float? = null,
    )
}
