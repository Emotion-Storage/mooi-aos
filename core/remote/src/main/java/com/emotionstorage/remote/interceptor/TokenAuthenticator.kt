package com.emotionstorage.remote.interceptor

import com.emotionstorage.remote.api.AuthApiService
import com.emotionstorage.remote.api.ReissueApiService
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val reissueApi: ReissueApiService
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        try {
            val newAccessToken = runBlocking {
                reissueApi.postReissue()
            }.data?.accessToken ?: return null
            return response.request.newBuilder()
                .header("Authorization", "Bearer $newAccessToken")
                .build()
        } catch (e: Exception) {
            Logger.e("Token reissue error, $e")
            return null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}
