package com.emotionstorage.auth.remote.api

import com.emotionstorage.auth.remote.request.LoginRequestBody
import com.emotionstorage.auth.remote.request.SignupRequestBody
import com.emotionstorage.auth.remote.response.LoginResponseData
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApiService {
    @POST("/users/login/google")
    suspend fun postGoogleLogin(
        @Body loginRequestBody: LoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body loginRequestBody: LoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/users/login/google")
    suspend fun postGoogleSignup(
        @Body signupRequestBody: SignupRequestBody
    ): ResponseDto<Unit>

    @POST("/users/login/kakao")
    suspend fun postKakaoSignup(
        @Body signupRequestBody: SignupRequestBody
    ): ResponseDto<Unit>

    // todo: add auth header
    @POST("/users/session")
    suspend fun postSession(): ResponseDto<Unit>

    // todo: add auth header
    @POST("auth/logout")
    suspend fun postLogout(): ResponseDto<Unit>

    // todo: add auth header
    @DELETE("mypage/account")
    suspend fun deleteAccount(): ResponseDto<Unit>
}