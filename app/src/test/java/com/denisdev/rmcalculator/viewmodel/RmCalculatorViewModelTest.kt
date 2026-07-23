package com.denisdev.rmcalculator.viewmodel

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.rmcalculator.base.BaseViewModelTest
import com.denisdev.rmcalculator.rmCalculator.RmCalculatorViewModel
import com.denisdev.rmcalculator.rmCalculator.RmUiData
import com.denisdev.rmcalculator.viewmodel.fakeRepository.FakeGetRm
import kotlinx.coroutines.flow.produceIn
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RmCalculatorViewModelTest: BaseViewModelTest() {
    private lateinit var sut: RmCalculatorViewModel
    private val fakeUseCase = FakeGetRm()

    @Before
    fun setUp() {
        sut = RmCalculatorViewModel(fakeUseCase)
    }

    @Test
    fun getRmSuccess() = runTest {
        val params = GetRm.Params("100", "8", Author.Brzycki(), WeightUnit.Kg, false)
        launchInBackground {
            fakeUseCase.emit(Result.success(RM(Author.Brzycki(), Weight(2f, WeightUnit.Kg))))
        }
        sut.getRm(params)

        sut.data.produceIn(this).test {
            Assert.assertNotEquals(RmUiData(), it)
        }
    }

    @Test
    fun getRmFailure() = runTest {
        val params = GetRm.Params("100---", "8---", Author.MCGlothin(), WeightUnit.Kg, true)
        launchInBackground {
            fakeUseCase.emit(Result.failure(Throwable()))
        }
        sut.getRm(params)

        sut.data.produceIn(this).test {
            Assert.assertEquals(RmUiData(), it)
        }
    }
}