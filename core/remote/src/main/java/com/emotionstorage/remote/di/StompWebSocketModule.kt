package com.emotionstorage.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.hildan.krossbow.stomp.StompClient
import org.hildan.krossbow.websocket.okhttp.OkHttpWebSocketClient
import java.time.Duration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StompWebSocketModule {
    @Singleton
    @Provides
    fun provideWebSocketClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): StompClient {
        return StompClient(
            webSocketClient =
                OkHttpWebSocketClient(
                    OkHttpClient
                        .Builder()
                        .pingInterval(Duration.ofSeconds(10))
                        .addInterceptor(loggingInterceptor)
                        .build(),
                ),
        )
    }
}
