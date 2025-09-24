package com.emotionstorage.tutorial.presentation

import androidx.lifecycle.ViewModel
import com.emotionstorage.auth.domain.model.Expectation
import com.emotionstorage.auth.domain.model.SignupForm
import com.emotionstorage.auth.domain.model.SignupForm.GENDER
import com.emotionstorage.auth.domain.usecase.SignupUseCase
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.User.AuthProvider
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

data class OnBoardingState(
    val signupForm: SignupForm = SignupForm(),
)

sealed class OnBoardingAction() {
    data class Initiate(val provider: AuthProvider, val idToken: String) : OnBoardingAction()

    data class InputNickname(val nickname: String) : OnBoardingAction()

    data class InputGenderAndBirth(val gender: GENDER, val birth: LocalDate) : OnBoardingAction()

    data class InputExpectations(val expectations: List<Expectation>) : OnBoardingAction()

    data class InputAgreedTerms(
        val isTermAgreed: Boolean,
        val isPrivacyAgreed: Boolean,
        val isMarketingAgreed: Boolean,
    ) : OnBoardingAction()

    object Signup : OnBoardingAction()
}

sealed class OnBoardingSideEffect {
    data class SignupSuccess(val provider: AuthProvider, val idToken: String) :
        OnBoardingSideEffect()

    object SignupFailed : OnBoardingSideEffect()
}

@HiltViewModel
class OnBoardingViewModel
    @Inject
    constructor(
        private val signup: SignupUseCase,
    ) : ViewModel(), ContainerHost<OnBoardingState, OnBoardingSideEffect> {
        override val container = container<OnBoardingState, OnBoardingSideEffect>(OnBoardingState())

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
                        action.isTermAgreed,
                        action.isPrivacyAgreed,
                        action.isMarketingAgreed,
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
            isTermAgreed: Boolean,
            isPrivacyAgreed: Boolean,
            isMarketingAgreed: Boolean,
        ) = intent {
            reduce {
                state.copy(
                    signupForm =
                        state.signupForm.copy(
                            isTermAgreed = isTermAgreed,
                            isPrivacyAgreed = isPrivacyAgreed,
                            isMarketingAgreed = isMarketingAgreed,
                        ),
                )
            }
        }

        private fun handleSignup() =
            intent {
                if (state.signupForm.provider == null) {
                    Logger.e("provider is null")
                    postSideEffect(OnBoardingSideEffect.SignupFailed)
                    return@intent
                }
                if (state.signupForm.idToken == null) {
                    Logger.e("idToken is null")
                    postSideEffect(OnBoardingSideEffect.SignupFailed)
                    return@intent
                }

                signup(state.signupForm).collect { result ->
                    when (result) {
                        is DataState.Loading -> {
                            // do nothing
                        }

                        is DataState.Success -> {
                            Logger.i(result.toString())
                            postSideEffect(
                                OnBoardingSideEffect.SignupSuccess(
                                    state.signupForm.provider!!,
                                    state.signupForm.idToken!!,
                                ),
                            )
                        }

                        is DataState.Error -> {
                            Logger.e(result.toString())
                            postSideEffect(OnBoardingSideEffect.SignupFailed)
                        }
                    }
                }
            }
    }
