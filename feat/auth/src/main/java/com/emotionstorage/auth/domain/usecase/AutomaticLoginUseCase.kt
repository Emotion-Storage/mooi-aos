package com.emotionstorage.auth.domain.usecase

interface AutomaticLoginUseCase {
    // TODO AutomaticLoginUseCase - 자동 로그인 기능 구현
    // domain 모듈의 GetUserUseCase를 의존하여 access token이 유효한지 확인
    suspend operator fun invoke(): Boolean
}