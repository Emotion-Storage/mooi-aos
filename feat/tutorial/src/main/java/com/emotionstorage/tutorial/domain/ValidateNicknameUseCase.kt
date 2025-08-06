package com.emotionstorage.tutorial.domain

import com.emotionstorage.common.DataResource
import javax.inject.Inject

enum class NicknameState(val message: String) {
    INVALID_EMPTY("2~8자리의 한글 또는 영문을 사용해주세요"),
    INVALID_CHAR("이름은 한글 또는 영문만 사용해주세요"),
    INVALID_LENGTH("이름은 최소 2글자 이상이어야 합니다"),
    VALID("사용 가능한 이름입니다.")
}

/**
 * Validate nickname use case
 * - check nickname is valid
 *
 * 이름/닉네임 형식 : 2~8자리의 한글 또는 영문, 중복 허용
 * 유효성 검사 case :
 * - 글자 수 부족
 * - 한글, 영문 외 다른 문자 사용
 * - 금칙어 입력 시(이 부분은 추후 정리 필요!!!)
 */
class ValidateNicknameUseCase @Inject constructor() {
    operator fun invoke(nickname: String): DataResource<NicknameState> {
        if (nickname.isNullOrEmpty())
            return DataResource.Success(NicknameState.INVALID_EMPTY)
        // todo: 모음만 입력되어도 유효하도록 정규식 수정
        if (!nickname.matches(Regex("^[a-zA-Zㄱ-ㅎ가-힣]*$")))
            return DataResource.Success(NicknameState.INVALID_CHAR)
        if (nickname.length < 2)
            return DataResource.Success(NicknameState.INVALID_LENGTH)
        return DataResource.Success(NicknameState.VALID)
    }
}