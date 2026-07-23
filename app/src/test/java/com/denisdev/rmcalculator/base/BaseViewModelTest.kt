package com.denisdev.rmcalculator.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    suspend fun<T> ReceiveChannel<T>.test(block: suspend (T) -> Unit) {
        block(receive())
        cancel()
    }
    fun TestScope.launchInBackground(block: suspend () -> Unit) = backgroundScope.launch(
        UnconfinedTestDispatcher()
    ) {
        block()
    }
}