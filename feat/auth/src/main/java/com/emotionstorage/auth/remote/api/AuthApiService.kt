package com.emotionstorage.auth.remote.api

import com.emotionstorage.auth.remote.request.GoogleLoginRequestBody
import com.emotionstorage.auth.remote.request.KakaoLoginRequestBody
import com.emotionstorage.auth.remote.request.GoogleSignupRequestBody
import com.emotionstorage.auth.remote.request.KakaoSignupRequestBody
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
        @Body loginRequestBody: GoogleLoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/google")
    suspend fun postGoogleSignup(
        @Body signupRequestBody: GoogleSignupRequestBody
    ): ResponseDto<SignupResponseData>


    @POST("/api/v1/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body loginRequestBody: KakaoLoginRequestBody
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/kakao")
    suspend fun postKakaoSignup(
        @Body signupRequestBody: KakaoSignupRequestBody
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