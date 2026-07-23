package com.denisdev.domain.formula

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Reps
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.rmcalculator.FakeAuthorRepository
import com.denisdev.domain.usecases.rmcalculator.GetRm
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import kotlin.math.round

@RunWith(Parameterized::class)
class RmFormulaTest(private val index: Int, private val author: Author) {

    companion object {
        private val defaultParams = RmParams(Weight(100F, WeightUnit.Kg), Reps(8))
        internal val list = arrayOf(
            Author.Epley(defaultParams),
            Author.OConner(defaultParams),
            Author.Lombardi(defaultParams),
            Author.Brzycki(defaultParams),
            Author.Mayhew(defaultParams),
            Author.Wathen(defaultParams),
            Author.Lander(defaultParams),
            Author.MCGlothin(defaultParams),
            Author.EpleyHighReps(defaultParams)
        )
        @JvmStatic
        @Parameters
        fun params(): Array<Array<Any>> {
            return arrayOf(
                arrayOf(0, list[0]),
                arrayOf(1, list[1]),
                arrayOf(2, list[2]),
                arrayOf(3, list[3]),
                arrayOf(4, list[4]),
                arrayOf(5, list[5]),
                arrayOf(6, list[6]),
                arrayOf(7, list[7]),
                arrayOf(8, list[8]),
            )
        }
    }

    @Test
    fun getAuthor() {
        runTest {
            GetRmImpl(FakeAuthorRepository()).invoke(GetRm.Params(
                author.params.weight.value.toString(),
                author.params.reps.value.toString(),
                author,
                author.params.weight.weightUnit,
                false
            )).collect {
                Assert.assertEquals(list[index], author)
            }
        }
    }

    @Test
    fun calculateRM() {
        val list = arrayOf(
            126F,
            120F,
            123F,
            124F,
            126F,
            128F,
            125F,
            124F,
            124F
        )
        runTest {
            GetRmImpl(FakeAuthorRepository(author)).invoke(GetRm.Params(
                author.params.weight.value.toString(),
                author.params.reps.value.toString(),
                author,
                author.params.weight.weightUnit,
                false
            )).collect {
                Assert.assertEquals(list[index], round(it.getOrThrow().weight.value))
            }
        }
    }

    @Test
    fun calculateRMWithZeroWeight() {
        runTest {
            GetRmImpl(FakeAuthorRepository()).invoke(GetRm.Params(
                "0.0",
                author.params.reps.value.toString(),
                author,
                WeightUnit.Kg,
                false
            )).collect {
                Assert.assertEquals(0F, it.getOrThrow().weight.value)
            }
        }
    }
    @Test
    fun calculateRMWithZeroReps() {
        runTest {
            GetRmImpl(FakeAuthorRepository()).invoke(GetRm.Params(
                author.params.weight.value.toString(),
                "0.0",
                author,
                WeightUnit.Kg,
                false
            )).collect {
                Assert.assertEquals(0F, it.getOrThrow().weight.value)
            }
        }
    }
}