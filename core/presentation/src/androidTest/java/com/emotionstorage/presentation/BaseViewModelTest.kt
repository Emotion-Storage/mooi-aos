@file:OptIn(ExperimentalCoroutinesApi::class, OrbitExperimental::class)

package com.emotionstorage.presentation

import com.emotionstorage.domain.common.ErrorCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.orbitmvi.orbit.annotation.OrbitExperimental

data class TestState(
    val dummy: Int = 0,
)

class TestViewModel : BaseViewModel<TestState>(TestState()) {
    fun throwInBaseIntent(error: Throwable) =
        baseIntent {
            throw error
        }

    fun throwInScope(error: Throwable) =
        baseViewModelScope.launch {
            throw error
        }
}

class BaseViewModelTest {
    private lateinit var viewModel: TestViewModel

    @Before
    fun setup() {
        viewModel = TestViewModel()
    }

    @Test
    fun baseIntent_should_catch_network_error() =
        runTest {
            val error =
                BaseException(
                    message = "network fail",
                    code = ErrorCode.NETWORK_ERROR,
                )

            viewModel.throwInBaseIntent(error)

            val effect = viewModel.container.sideEffectFlow.first()
            assertEquals(BaseSideEffect.NetworkError, effect)
        }

    @Test
    fun baseIntent_should_catch_auth_error() =
        runTest {
            val error =
                BaseException(
                    message = "expired",
                    code = ErrorCode.UNAUTHORIZED,
                )

            viewModel.throwInBaseIntent(error)

            val effect = viewModel.container.sideEffectFlow.first()
            assertEquals(BaseSideEffect.SessionExpired, effect)
        }

    @Test
    fun baseIntent_should_catch_internal_server_error() =
        runTest {
            val error =
                BaseException(
                    message = "server fail",
                    code = ErrorCode.INTERNAL_SERVER_ERROR,
                )

            viewModel.throwInBaseIntent(error)

            val effect = viewModel.container.sideEffectFlow.first()
            assertEquals(BaseSideEffect.TemporalError(ErrorCode.INTERNAL_SERVER_ERROR), effect)
        }

    @Test
    fun baseViewModelScope_should_catch_internal_server_error() =
        runTest {
            val error =
                BaseException(
                    message = "server fail",
                    code = ErrorCode.INTERNAL_SERVER_ERROR,
                )

            viewModel.throwInScope(error)

            val effect = viewModel.container.sideEffectFlow.first()
            assertEquals(BaseSideEffect.TemporalError(ErrorCode.INTERNAL_SERVER_ERROR), effect)
        }
}
