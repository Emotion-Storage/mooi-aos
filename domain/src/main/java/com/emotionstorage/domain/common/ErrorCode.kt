package com.emotionstorage.domain.common

import com.emotionstorage.domain.common.ErrorCode.ACCESS_TOKEN_EXPIRED
import com.emotionstorage.domain.common.ErrorCode.ACCESS_TOKEN_INVALID
import com.emotionstorage.domain.common.ErrorCode.REFRESH_TOKEN_EXPIRED
import com.emotionstorage.domain.common.ErrorCode.REFRESH_TOKEN_NOT_FOUND
import com.emotionstorage.domain.common.ErrorCode.UNAUTHORIZED

enum class ErrorCode {
    // common
    UNKNOWN,
    INTERNAL_SERVER_ERROR,
    NETWORK_ERROR,

    // auth remote
    USER_NOT_FOUND,
    NEED_SIGN_UP,
    ALREADY_REGISTERED_WITH_GOOGLE,
    ALREADY_REGISTERED_WITH_KAKAO,
    INVALID_ID_TOKEN,
    INVALID_KAKAO_ACCESS_TOKEN,
    ACCESS_TOKEN_EXPIRED,
    ACCESS_TOKEN_INVALID,
    REFRESH_TOKEN_EXPIRED,
    REFRESH_TOKEN_NOT_FOUND,
    UNAUTHORIZED,
    INVALID_NICKNAME,

    // chat remote
    CHAT_ROOM_NOT_FOUND,
    CHAT_ROOM_ACCESS_DENIED,
    TICKET_NOT_ENOUGH,

    // time capsule remote
    TIME_CAPSULE_NOT_FOUND,
    TIME_CAPSULE_IS_NOT_OWNED,
    TIME_CAPSULE_FAVORITE_LIMIT_EXCEEDED,
    TIME_CAPSULE_KEY_NOT_ENOUGH,
    TIME_CAPSULE_OPEN_RULE_NOT_FOUND,
    TIME_CAPSULE_NOT_TEMP_SAVE,
    TIME_CAPSULE_DRAFT_EXPIRED,
    TIME_CAPSULE_OPEN_DATE_BEFORE_STORED_AT,
    TIME_CAPSULE_OPEN_DATE_AFTER_LIMIT,

    // daily report remote
    REPORT_NOT_FOUND,
    DAILY_REPORT_NOT_FOUND,
    INVALID_DATE_FORMAT,

    // notification settings remote
    EMOTION_REMINDER_DAYS_REQUIRED,
    EMOTION_REMINDER_TIME_REQUIRED,

    // attendance reward remote
    ALREADY_GET_ATTENDANCE_REWARD,
    EXPIRED_ATTENDANCE_REWARD,
    ;

    companion object {
        fun toErrorCode(code: String) = ErrorCode.entries.firstOrNull { it.name == code } ?: UNKNOWN
    }
}

fun ErrorCode.isAuthError() =
    this in
        listOf<ErrorCode>(
            ACCESS_TOKEN_EXPIRED,
            ACCESS_TOKEN_INVALID,
            REFRESH_TOKEN_EXPIRED,
            REFRESH_TOKEN_NOT_FOUND,
            UNAUTHORIZED,
        )
