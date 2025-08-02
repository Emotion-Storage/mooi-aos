package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.emotionstorage.remote.interceptor.RequestHeaderInterceptor
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
    // todo: add mooi backend base url
    private const val BASE_URL = "~~~~/api/v1"

    @Singleton
    @Provides
    fun provideRetrofit(
        loggingInterceptor: HttpLoggingInterceptor,
        requestHeaderInterceptor: RequestHeaderInterceptor
    ) = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            Json {
                ignoreUnknownKeys
            }.asConverterFactory("application/json".toMediaType()),
        )
        .client(
            OkHttpClient
                .Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(requestHeaderInterceptor)
                .build()
        )
        .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideRequestHeaderInterceptor(sessionLocalDataSource: SessionLocalDataSource) =
        RequestHeaderInterceptor(sessionLocalDataSource)
}
