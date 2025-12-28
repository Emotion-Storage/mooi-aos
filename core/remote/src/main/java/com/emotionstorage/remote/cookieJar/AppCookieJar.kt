package com.emotionstorage.remote.cookieJar

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class AppCookieJar(
    private val sessionLocalDataSource: SessionLocalDataSource
) : CookieJar {

    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
        // todo: add refresh token to cookie
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
        // todo: save refresh token to data store
    }
}

