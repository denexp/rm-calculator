package com.denisdev.domain.rmcalculator

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetRMsTestImpl {
    lateinit var useCase: GetRm

    @Before
    fun setUp() {
        useCase = GetRmImpl(FakeAuthorRepository())
    }
    @Test
    fun success() {
        val params = GetRm.Params("100", "8", Author.Mayhew(), WeightUnit.Kg, true)

        runTest {
            useCase(params).onEach { result ->
                Assert.assertTrue(result.isSuccess)
            }.collect()
        }
    }

    @Test
    fun failureWrongParameters() {
        val params = GetRm.Params("TEST", "TEST", Author.Mayhew(), WeightUnit.Kg, true)

        runTest {
            useCase(params).onEach { result ->
                Assert.assertTrue(result.isSuccess)
                Assert.assertTrue(result.getOrThrow().author.calculate().value == 0f)
            }.collect()
        }
    }
    @Test
    fun failureNoParameters() {
        runTest {
            useCase().onEach { result ->
                Assert.assertTrue(result.isFailure)
            }.collect()
        }
    }
}