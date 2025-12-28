package com.emotionstorage.remote.di

import com.emotionstorage.remote.BuildConfig
import com.emotionstorage.remote.api.AuthApiService
import com.emotionstorage.remote.api.ReissueApiService
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
    private const val BASE_URL = "http://${BuildConfig.MOOI_DEV_SERVER_URL}"
    private const val TIMEOUT = 20L

    private val json =
        Json {
            ignoreUnknownKeys = true // Common configuration
            isLenient = true
            prettyPrint = true // For debugging, optional
        }

    /**
     * auth retrofit to prevent dependency cycle
     */
    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideAuthRetrofit(
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .build())
            .build()
    }

    @Provides
    @Singleton
    fun provideReissueApi(
        @Named("AuthRetrofit") retrofit: Retrofit
    ): ReissueApiService = retrofit.create(ReissueApiService::class.java)
}
