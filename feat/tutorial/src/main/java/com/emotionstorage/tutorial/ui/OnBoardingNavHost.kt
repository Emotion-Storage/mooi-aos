package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.emotionstorage.domain.common.ErrorCode
import com.emotionstorage.domain.model.User.AuthProvider
import com.emotionstorage.presentation.BaseSideEffect
import com.emotionstorage.tutorial.presentation.OnBoardingAction
import com.emotionstorage.tutorial.presentation.OnBoardingSideEffect
import com.emotionstorage.tutorial.presentation.OnBoardingState
import com.emotionstorage.tutorial.presentation.OnBoardingViewModel
import com.emotionstorage.tutorial.ui.modal.DuplicateAccountModal
import com.emotionstorage.tutorial.ui.modal.InquireSignupErrorModal
import com.emotionstorage.tutorial.ui.modal.SocialTokenExpiredModal
import com.emotionstorage.tutorial.ui.onBoarding.AgreeTermsScreen
import com.emotionstorage.tutorial.ui.onBoarding.ExpectationsScreen
import com.emotionstorage.tutorial.ui.onBoarding.GenderBirthScreen
import com.emotionstorage.tutorial.ui.terms.MarketingUsageDetailScreen
import com.emotionstorage.tutorial.ui.onBoarding.NicknameScreen
import com.emotionstorage.tutorial.ui.terms.PrivacyPolicyDetailScreen
import com.emotionstorage.tutorial.ui.terms.TermDetailScreen
import com.emotionstorage.ui.component.loading.LoadingOverlay
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding destinations
 */
enum class OnBoardingRoute(
    val route: String,
) {
    NICKNAME("on_boarding/nickname"),
    GENDER_BIRTH("on_boarding/gender_birth"),
    EXPECTATIONS("on_boarding/expectations"),
    AGREE_TERMS("on_boarding/agree_terms"),
    TERM_DETAIL("on_boarding/agree_terms/term_detail"),
    PRIVACY_DETAIL("on_boarding/agree_terms/privacy_detail"),
    MARKETING_DETAIL("on_boarding/agree_terms/marketing_detail"),
}

private sealed class OnBoardingModalState {
    object None : OnBoardingModalState()

    object SocialTokenExpired : OnBoardingModalState()

    object DuplicateAccount : OnBoardingModalState()

    data class SignupError(
        val errorCode: ErrorCode,
        val throwable: Throwable,
    ) : OnBoardingModalState()
}

@Composable
fun OnBoardingNavHost(
    provider: AuthProvider,
    idToken: String,
    modifier: Modifier = Modifier,
    sharedViewModel: OnBoardingViewModel = hiltViewModel(),
    navToSignupComplete: (provider: AuthProvider, idToken: String) -> Unit = { _, _ -> },
    navToBack: () -> Unit = {},
) {
    val navController = rememberNavController()
    val state by sharedViewModel.container.stateFlow.collectAsState()
    var modalState by remember { mutableStateOf<OnBoardingModalState>(OnBoardingModalState.None) }

    LaunchedEffect(provider, idToken) {
        sharedViewModel.onAction(OnBoardingAction.Initiate(provider, idToken))
    }
    LaunchedEffect(Unit) {
        sharedViewModel.container.sideEffectFlow.collect { sideEffect ->
            when (sideEffect) {
                is OnBoardingSideEffect.SignupSuccess -> {
                    navToSignupComplete(sideEffect.provider, sideEffect.idToken)
                }

                is OnBoardingSideEffect.SocialTokenExpired -> {
                    modalState = OnBoardingModalState.SocialTokenExpired
                }

                is OnBoardingSideEffect.DuplicateAccount -> {
                    modalState = OnBoardingModalState.DuplicateAccount
                }

                is BaseSideEffect.TemporalError -> {
                    modalState =
                        OnBoardingModalState.SignupError(sideEffect.code, sideEffect.throwable)
                }

                is BaseSideEffect.NetworkError -> {
                    // todo: add network toast
                }
            }
        }
    }

    if (state.isLoading) {
        LoadingOverlay()
    }
    StatelessOnBoardingNavHost(
        modifier = modifier,
        navController = navController,
        state = state,
        onAction = sharedViewModel::onAction,
        navToBack = navToBack,
    )

    when (modalState) {
        OnBoardingModalState.None -> {}

        OnBoardingModalState.SocialTokenExpired -> {
            SocialTokenExpiredModal(
                onDismissRequest = {
                    modalState = OnBoardingModalState.None
                },
                onConfirm = {
                    // nav back to login screen
                    navToBack()
                },
            )
        }

        OnBoardingModalState.DuplicateAccount -> {
            DuplicateAccountModal(
                onDismissRequest = {
                    modalState = OnBoardingModalState.None
                },
                onConfirm = {
                    // nav back to login screen
                    navToBack()
                },
            )
        }

        is OnBoardingModalState.SignupError -> {
            val inquireModalState = modalState as OnBoardingModalState.SignupError
            InquireSignupErrorModal(
                inquireModalState.errorCode,
                inquireModalState.throwable,
                onDismissRequest = {
                    modalState = OnBoardingModalState.None
                },
            )
        }
    }
}

@Composable
private fun StatelessOnBoardingNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    state: OnBoardingState = OnBoardingState(),
    onAction: (OnBoardingAction) -> Unit = {},
    navToBack: () -> Unit = {},
) {
    NavHost(
        navController,
        startDestination = OnBoardingRoute.NICKNAME.route,
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.backgroundDefault),
    ) {
        OnBoardingRoute.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    OnBoardingRoute.NICKNAME -> {
                        NicknameScreen(
                            nickname = state.signupForm.nickname,
                            onNicknameInputComplete = { nickname ->
                                onAction(OnBoardingAction.InputNickname(nickname))
                            },
                            navToGenderBirth = {
                                navController.navigate(OnBoardingRoute.GENDER_BIRTH.route)
                            },
                            navToBack = {
                                // call navToBack() instead of navController.popBackStack(), to exit from on boarding nav controller
                                navToBack()
                            },
                        )
                    }

                    OnBoardingRoute.GENDER_BIRTH -> {
                        GenderBirthScreen(
                            nickname = state.signupForm.nickname!!,
                            gender = state.signupForm.gender,
                            birth = state.signupForm.birthday,
                            onGenderBirthInputComplete = { gender, birth ->
                                onAction(OnBoardingAction.InputGenderAndBirth(gender, birth))
                            },
                            navToExpectations = {
                                navController.navigate(OnBoardingRoute.EXPECTATIONS.route)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.EXPECTATIONS -> {
                        ExpectationsScreen(
                            expectations = state.signupForm.expectations,
                            onExpectationsSelectComplete = { expectations ->
                                onAction(OnBoardingAction.InputExpectations(expectations))
                            },
                            navToAgreeTerms = {
                                navController.navigate(OnBoardingRoute.AGREE_TERMS.route)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.AGREE_TERMS -> {
                        AgreeTermsScreen(
                            isAllAgreed = state.isAllAgreed,
                            isTermAgreed = state.signupForm.isTermAgreed,
                            isPrivacyAgreed = state.signupForm.isPrivacyAgreed,
                            isMarketingAgreed = state.signupForm.isMarketingAgreed,
                            isAgeAgreed = state.isAgeAgreed,
                            onAgreeTermsInputComplete = {
                                    isAllAgreed,
                                    isTermAgreed,
                                    isPrivacyAgreed,
                                    isMarketingAgreed,
                                    isAgeAgreed,
                                ->
                                onAction(
                                    OnBoardingAction.InputAgreedTerms(
                                        isAllAgreed,
                                        isTermAgreed,
                                        isPrivacyAgreed,
                                        isMarketingAgreed,
                                        isAgeAgreed,
                                    ),
                                )
                            },
                            onSignup = {
                                onAction(OnBoardingAction.Signup)
                            },
                            navToBack = {
                                navController.popBackStack()
                            },
                            navToTermDetail = {
                                navController.navigate(OnBoardingRoute.TERM_DETAIL.route)
                            },
                            navToPrivacyDetail = {
                                navController.navigate(OnBoardingRoute.PRIVACY_DETAIL.route)
                            },
                            navToMarketingDetail = {
                                navController.navigate(OnBoardingRoute.MARKETING_DETAIL.route)
                            },
                        )
                    }

                    OnBoardingRoute.TERM_DETAIL -> {
                        TermDetailScreen(
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.PRIVACY_DETAIL -> {
                        PrivacyPolicyDetailScreen(
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }

                    OnBoardingRoute.MARKETING_DETAIL -> {
                        MarketingUsageDetailScreen(
                            navToBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}
