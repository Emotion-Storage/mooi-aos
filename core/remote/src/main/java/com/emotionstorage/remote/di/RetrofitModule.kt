package com.emotionstorage.remote.di

import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.emotionstorage.remote.BuildConfig
import com.emotionstorage.remote.interceptor.RequestHeaderInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    private const val BASE_URL = "http://${BuildConfig.MOOI_DEV_SERVER_URL}"
    private const val TIMEOUT = 20L

    private val json =
        Json {
            ignoreUnknownKeys = true // Common configuration
            isLenient = true
            prettyPrint = true // For debugging, optional
        }

    @Singleton
    @Provides
    fun provideRetrofit(
        requestHeaderInterceptor: RequestHeaderInterceptor,
    ): Retrofit {
        val cloneErrorBodyInterceptor = Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)

            if (response.isSuccessful) return@Interceptor response

            // get error body
            val sourceBody = response.body
            if (sourceBody == null) {
                return@Interceptor response
            }

            // clone error body stream to byte array input stream
            val bytes = sourceBody.bytes()
            val contentType = sourceBody.contentType()
            val newResponseBody = bytes.inputStream().use {
                ResponseBody.create(contentType, bytes)
            }

            response.newBuilder()
                .body(newResponseBody)
                .build()
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
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
                    .addInterceptor(requestHeaderInterceptor)
                    .addInterceptor(cloneErrorBodyInterceptor)
                    .addInterceptor(loggingInterceptor)
                    .build(),
            ).build()
    }

    @Singleton
    @Provides
    fun provideRequestHeaderInterceptor(sessionLocalDataSource: SessionLocalDataSource) =
        RequestHeaderInterceptor(sessionLocalDataSource)
}
