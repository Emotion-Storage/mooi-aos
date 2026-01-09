package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.local.SessionLocalDataSource
import com.emotionstorage.remote.BuildConfig
import com.emotionstorage.remote.api.ReissueApiService
import com.emotionstorage.remote.cookieJar.AppCookieJar
import com.emotionstorage.remote.interceptor.FailResponseInterceptor
import com.emotionstorage.remote.interceptor.RequestHeaderInterceptor
import com.emotionstorage.remote.interceptor.TokenAuthenticator
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "http://${BuildConfig.MOOI_DEV_SERVER_URL}"
    private const val TIMEOUT = 20L

    @Singleton
    @Provides
    fun provideRetrofit(
        json: Json,
        appCookieJar: AppCookieJar,
        requestHeaderInterceptor: RequestHeaderInterceptor,
        failResponseInterceptor: FailResponseInterceptor,
        tokenAuthenticator: TokenAuthenticator,
    ): Retrofit {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
            }

        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient
                    .Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .cookieJar(appCookieJar)
                    .addNetworkInterceptor(loggingInterceptor)
                    .addInterceptor(requestHeaderInterceptor)
                    .addInterceptor(failResponseInterceptor)
                    .authenticator(tokenAuthenticator)
                    .build(),
            ).build()
    }

    @Singleton
    @Provides
    fun provideRequestHeaderInterceptor(sessionLocalDataSource: SessionLocalDataSource) =
        RequestHeaderInterceptor(sessionLocalDataSource)

    @Singleton
    @Provides
    fun provideFailResponseHeaderInterceptor(json: Json) = FailResponseInterceptor(json)

    @Singleton
    @Provides
    fun provideTokenAuthenticator(
        reissueApiService: ReissueApiService,
        sessionLocalDataSource: SessionLocalDataSource,
    ) = TokenAuthenticator(reissueApiService, sessionLocalDataSource)
}
