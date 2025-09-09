package com.emotionstorage.auth.remote.api

import com.emotionstorage.auth.remote.request.LoginRequestBody
import com.emotionstorage.auth.remote.request.SignupRequestBody
import com.emotionstorage.auth.remote.response.LoginResponseData
import com.emotionstorage.auth.remote.response.SignupResponseData
import com.emotionstorage.remote.interceptor.AuthRequest
import com.emotionstorage.remote.response.ResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/users/login/google")
    suspend fun postGoogleLogin(
        @Body loginRequestBody: LoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/google")
    suspend fun postGoogleSignup(
        @Body signupRequestBody: SignupRequestBody
    ): ResponseDto<SignupResponseData>


    @POST("/api/v1/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body loginRequestBody: LoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/kakao")
    suspend fun postKakaoSignup(
        @Body signupRequestBody: SignupRequestBody
    ): ResponseDto<SignupResponseData>

    @AuthRequest
    @POST("/auth/session")
    suspend fun postSession(): ResponseDto<Unit>

    @AuthRequest
    @POST("auth/logout")
    suspend fun postLogout(): ResponseDto<Unit>

    @AuthRequest
    @DELETE("mypage/account")
    suspend fun deleteAccount(): ResponseDto<Unit>
}