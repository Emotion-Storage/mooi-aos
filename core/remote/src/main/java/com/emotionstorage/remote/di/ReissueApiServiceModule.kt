package com.emotionstorage.remote.di

import com.emotionstorage.remote.BuildConfig
import com.emotionstorage.remote.api.ReissueApiService
import com.emotionstorage.remote.cookieJar.AppCookieJar
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReissueApiServiceModule {
    private val BASE_URL =
        "https://${if (BuildConfig.DEBUG) BuildConfig.MOOI_DEV_SERVER_URL else BuildConfig.MOOI_PROD_SERVER_URL}/"
    private const val TIMEOUT = 20L

    /**
     * auth retrofit to prevent dependency cycle
     */
    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(
        json: Json,
        appCookieJar: AppCookieJar,
    ): Retrofit =
        Retrofit
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
                    .build(),
            ).build()

    @Provides
    @Singleton
    fun provideReissueApi(
        @Named("AuthRetrofit") retrofit: Retrofit,
    ): ReissueApiService = retrofit.create(ReissueApiService::class.java)
}
