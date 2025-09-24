package com.emotionstorage.remote.interceptor

import com.emotionstorage.data.dataSource.SessionLocalDataSource
import com.orhanobut.logger.Logger
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AuthRequest

class RequestHeaderInterceptor
    @Inject
    constructor(
        private val sessionLocalDataSource: SessionLocalDataSource,
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            // add default headers
            val requestBuilder =
                chain
                    .request()
                    .newBuilder()
                    .apply {
                        addHeader("Content-Type", "application/json")
                        addHeader("Accept", "application/json")
                    }

            // always add authorization header if session exists
            runBlocking {
                sessionLocalDataSource.getSession()?.accessToken
            }?.run {
                Logger.d("add authorization header, $this")
                requestBuilder.addHeader("Authorization", "Bearer " + this)
            }

            // add authorization header if @AuthRequest annotation is added
//        chain.request().tag(Invocation::class.java)?.let { invocation ->
//            containedOnInvocation(invocation).forEach { annotation ->
//                requestBuilder.apply {
//                    when (annotation) {
//                        is AuthRequest -> {
//                            runBlocking {
//                                sessionLocalDataSource.getSession()?.accessToken
//                            }?.run {
//                                Logger.d("add authorization header, $this")
//                                addHeader("Authorization", "Bearer " + this)
//                            }
//                        }
//                    }
//                }
//            }
//        }

            Logger.d("request headers: ${requestBuilder.build().headers}")
            return chain.proceed(requestBuilder.build())
        }

        private fun containedOnInvocation(invocation: Invocation): Set<Annotation> =
            invocation.method().annotations.toSet()
    }
