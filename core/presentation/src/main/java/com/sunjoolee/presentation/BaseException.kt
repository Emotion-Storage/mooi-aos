package com.sunjoolee.presentation

import com.emotionstorage.domain.common.ErrorCode

class BaseException(
    override val message: String?,
    val code: ErrorCode,
    val throwable: Throwable? = null,
) : Throwable()
