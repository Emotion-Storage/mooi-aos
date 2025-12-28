package com.emotionstorage.tutorial.presentation

import com.emotionstorage.domain.model.Expectation
import com.emotionstorage.domain.model.SignupForm
import com.emotionstorage.domain.model.SignupForm.GENDER
import com.emotionstorage.domain.useCase.auth.SignupUseCase
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.presentation.BaseException
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

data class OnBoardingState(
    val isLoading: Boolean = false,
    val signupForm: SignupForm = SignupForm(),
    // states not included in signup form
    val isAllAgreed: Boolean? = null,
    val isAgeAgreed: Boolean? = null,
)

sealed class OnBoardingAction {
    data class Initiate(
        val provider: AuthProvider,
        val idToken: String,
    ) : OnBoardingAction()

    data class InputNickname(
        val nickname: String,
    ) : OnBoardingAction()

    data class InputGenderAndBirth(
        val gender: GENDER,
        val birth: LocalDate,
    ) : OnBoardingAction()

    data class InputExpectations(
        val expectations: List<Expectation>,
    ) : OnBoardingAction()

    data class InputAgreedTerms(
        val isAllAgreed: Boolean,
        val isTermAgreed: Boolean,
        val isPrivacyAgreed: Boolean,
        val isMarketingAgreed: Boolean,
        val isAgeAgreed: Boolean,
    ) : OnBoardingAction()

    object Signup : OnBoardingAction()
}

sealed class OnBoardingSideEffect : BaseSideEffect {
    data class SignupSuccess(
        val provider: AuthProvider,
        val idToken: String,
    ) : OnBoardingSideEffect()

    object SocialTokenExpired : OnBoardingSideEffect()

    object DuplicateAccount : OnBoardingSideEffect()
}

@HiltViewModel
class OnBoardingViewModel
    @Inject
    constructor(
        private val signup: SignupUseCase,
    ) : BaseViewModel<OnBoardingState>(
            OnBoardingState(),
        ) {
        fun onAction(action: OnBoardingAction) {
            when (action) {
                is OnBoardingAction.Initiate -> {
                    handleInitiate(action.provider, action.idToken)
                }

                is OnBoardingAction.InputNickname -> {
                    handleInputNickname(action.nickname)
                }

                is OnBoardingAction.InputGenderAndBirth -> {
                    handleInputGenderAndBirth(action.gender, action.birth)
                }

                is OnBoardingAction.InputExpectations -> {
                    handleInputExpectations(action.expectations)
                }

                is OnBoardingAction.InputAgreedTerms -> {
                    handleInputAgreedTerms(
                        action.isAllAgreed,
                        action.isTermAgreed,
                        action.isPrivacyAgreed,
                        action.isMarketingAgreed,
                        action.isAgeAgreed,
                    )
                }

                is OnBoardingAction.Signup -> {
                    handleSignup()
                }
            }
        }

        private fun handleInitiate(
            provider: AuthProvider,
            idToken: String,
        ) = intent {
            reduce {
                state.copy(signupForm = state.signupForm.copy(provider = provider, idToken = idToken))
            }
        }

        private fun handleInputNickname(nickname: String) =
            intent {
                reduce {
                    state.copy(signupForm = state.signupForm.copy(nickname = nickname))
                }
            }

        private fun handleInputGenderAndBirth(
            gender: GENDER,
            birth: LocalDate,
        ) = intent {
            reduce {
                state.copy(signupForm = state.signupForm.copy(gender = gender, birthday = birth))
            }
        }

        private fun handleInputExpectations(expectations: List<Expectation>) =
            intent {
                reduce {
                    state.copy(signupForm = state.signupForm.copy(expectations = expectations))
                }
            }

        private fun handleInputAgreedTerms(
            isAllAgreed: Boolean,
            isTermAgreed: Boolean,
            isPrivacyAgreed: Boolean,
            isMarketingAgreed: Boolean,
            isAgeAgreed: Boolean,
        ) = intent {
            reduce {
                state.copy(
                    signupForm =
                        state.signupForm.copy(
                            isTermAgreed = isTermAgreed,
                            isPrivacyAgreed = isPrivacyAgreed,
                            isMarketingAgreed = isMarketingAgreed,
                        ),
                    isAllAgreed = isAllAgreed,
                    isAgeAgreed = isAgeAgreed,
                )
            }
        }

        private fun handleSignup() =
            baseIntent {
                // snapshot state value
                val provider = state.signupForm.provider
                val idToken = state.signupForm.idToken

                if (provider == null || idToken == null) {
                    postSideEffect(OnBoardingSideEffect.SocialTokenExpired)
                } else {
                    reduce {
                        state.copy(isLoading = true)
                    }
                    signup(state.signupForm).handle(
                        onSuccess = {
                            reduce {
                                state.copy(isLoading = false)
                            }
                            postSideEffect(
                                OnBoardingSideEffect.SignupSuccess(provider, idToken),
                            )
                        },
                        onError = { throwable, code, data ->
                            reduce {
                                state.copy(isLoading = false)
                            }
                            if (code == ErrorCode.INVALID_ID_TOKEN ||
                                code == ErrorCode.INVALID_KAKAO_ACCESS_TOKEN
                            ) {
                                postSideEffect(OnBoardingSideEffect.SocialTokenExpired)
                            } else if (code == ErrorCode.ALREADY_REGISTERED_WITH_GOOGLE ||
                                code == ErrorCode.ALREADY_REGISTERED_WITH_KAKAO
                            ) {
                                postSideEffect(OnBoardingSideEffect.DuplicateAccount)
                            } else {
                                throw BaseException(
                                    message = throwable.message,
                                    code = code,
                                    cause = throwable,
                                )
                            }
                        },
                    )
                }
            }
    }
