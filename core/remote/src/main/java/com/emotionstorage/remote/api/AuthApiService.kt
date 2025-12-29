package com.emotionstorage.remote.api

import com.emotionstorage.remote.request.auth.GoogleLoginRequestBody
import com.emotionstorage.remote.request.auth.GoogleSignupRequestBody
import com.emotionstorage.remote.request.auth.KakaoLoginRequestBody
import com.emotionstorage.remote.request.auth.KakaoSignupRequestBody
import com.emotionstorage.remote.response.ResponseDto
import com.emotionstorage.remote.response.auth.LoginResponseData
import com.emotionstorage.remote.response.auth.SignupResponseData
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/v1/users/login/google")
    suspend fun postGoogleLogin(
        @Body loginRequestBody: GoogleLoginRequestBody,
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/google")
    suspend fun postGoogleSignup(
        @Body signupRequestBody: GoogleSignupRequestBody,
    ): ResponseDto<SignupResponseData>

    @POST("/api/v1/users/login/kakao")
    suspend fun postKakaoLogin(
        @Body loginRequestBody: KakaoLoginRequestBody,
    ): ResponseDto<LoginResponseData>

    @POST("/api/v1/users/signup/kakao")
    suspend fun postKakaoSignup(
        @Body signupRequestBody: KakaoSignupRequestBody,
    ): ResponseDto<SignupResponseData>

    @GET("/auth/session")
    suspend fun getAuthSession(): ResponseDto<Unit>

    @DELETE("/api/v1/mypage/logout")
    suspend fun postLogout(): ResponseDto<Unit>

    @DELETE("/api/v1/mypage/account")
    suspend fun deleteAccount(): ResponseDto<Unit>
}
