package com.emotionstorage.remote.interceptor

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.remote.response.CustomHttpException
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.ResponseStatus
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.Response
import java.time.LocalDateTime
import javax.inject.Inject

private const val MAX_TRY_COUNT = 3

// todo: refresh token & retry on UNAUTHORIZED error
// todo: retry request for max 3 times on error
class FailResponseInterceptor @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource,
) : Interceptor {
    private val json = Json { ignoreUnknownKeys = true }

    override fun intercept(chain: Interceptor.Chain): Response {
        val httpRequest = chain.request()
        val httpResponse = chain.proceed(httpRequest)
        // return successful response
        if (httpResponse.code in 200..226) return httpResponse

        // parse error response body & throw custom error
        val httpResponseBody = httpResponse.peekBody(Long.MAX_VALUE).string()

        val responseDto = runCatching {
            json.decodeFromString<ResponseDto<String>>(httpResponseBody)
        }.getOrNull()

        throw CustomHttpException(
            status = ResponseStatus.fromCode(responseDto?.status ?: httpResponse.code),
            code = responseDto?.code ?: "unknown(${httpResponse.code})",
            message = responseDto?.message ?: httpResponse.message,
            data = responseDto?.data ?: httpResponseBody,
            timestamp = responseDto?.timestamp ?: LocalDateTime.now(),
        )
        return httpResponse
    }
}
