package com.emotionstorage.remote.cookieJar

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

private const val REFRESH_TOKEN_NAME = "refreshToken"

class AppCookieJar(
    private val sessionLocalDataSource: SessionLocalDataSource,
) : CookieJar {

    private val scope = CoroutineScope(Dispatchers.IO)
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        if (url.encodedPath.contains("/auth/reissue")) {
            // get refresh token from data store if reissue
            val refreshToken = runBlocking { sessionLocalDataSource.getRefreshToken() ?: "" }
            return listOf(
                Cookie
                    .Builder()
                    .name(REFRESH_TOKEN_NAME)
                    .value(refreshToken)
                    .domain(url.host)
                    .path("/")
                    .httpOnly()
                    .secure()
                    .build(),
            )
        } else {
            return cookieStore[url.host] ?: emptyList()
        }
    }

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>,
    ) {
        Logger.d("save cookies from response: $cookies")
        cookieStore[url.host] = cookies

        // save refresh token to data store
        cookies.find { it.name == REFRESH_TOKEN_NAME }?.let { cookie ->
            scope.launch {
                sessionLocalDataSource.saveRefreshToken(cookie.value)
            }
        }
    }
}
