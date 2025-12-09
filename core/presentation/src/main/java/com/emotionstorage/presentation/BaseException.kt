package com.emotionstorage.presentation

import com.emotionstorage.domain.common.ErrorCode

class BaseException(
    val code: ErrorCode,
    override val message: String?,
    override val cause: Throwable? = null,
) : Throwable(message, cause)
