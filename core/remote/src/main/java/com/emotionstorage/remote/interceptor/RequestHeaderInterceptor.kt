package com.emotionstorage.remote.interceptor

import com.emotionstorage.data.dataSource.UserLocalDataSource
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
    private val userLocalDataSource: UserLocalDataSource
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

        // add authorization header if @AuthRequest annotation is added
        chain.request().tag(Invocation::class.java)?.let { invocation ->
            containedOnInvocation(invocation).forEach { annotation ->
                requestBuilder.apply {
                    when (annotation) {
                        is AuthRequest -> {
                            runBlocking {
                                userLocalDataSource.getUser()?.accessToken
                            }?.run {
                                addHeader("Authorization", this)
                            }
                        }
                    }
                }
            }
        }
        return chain.proceed(requestBuilder.build())
    }

    private fun containedOnInvocation(invocation: Invocation): Set<Annotation> =
        invocation.method().annotations.toSet()
}