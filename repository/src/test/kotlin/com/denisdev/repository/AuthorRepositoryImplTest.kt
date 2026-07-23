package com.denisdev.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Reps
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthorRepositoryImplTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = AuthorRepositoryImpl(testDispatcher)

    @Test
    fun `when reps is 0, return default author type with 0 weight`() = runTest {
        val params = RmParams(weight = Weight(100f, WeightUnit.Kg), reps = Reps(0))
        val defaultAuthor = Author.Epley(params)
        
        val result = repository.getAuthor(params, autoFx = true, default = defaultAuthor)
        
        assertTrue(result is Author.Epley)
        assertEquals(0f, result.params.weight.value)
        assertEquals(0, result.params.reps.value)
        assertEquals(WeightUnit.Kg, result.params.weightUnit)
    }

    @Test
    fun `when autoFx is false, return default author type with provided params`() = runTest {
        val params = RmParams(reps = Reps(5))
        val defaultAuthor = Author.Lander(RmParams())
        
        val result = repository.getAuthor(params, autoFx = false, default = defaultAuthor)
        
        assertTrue(result is Author.Lander)
        assertEquals(params, result.params)
    }

    @Test
    fun `when autoFx is true and reps is between 1 and 10, return Brzycki`() = runTest {
        val params = RmParams(reps = Reps(10))
        val defaultAuthor = Author.Epley(params)
        
        val result = repository.getAuthor(params, autoFx = true, default = defaultAuthor)
        
        assertTrue(result is Author.Brzycki)
        assertEquals(params, result.params)
    }

    @Test
    fun `when autoFx is true and reps is between 11 and limit, return Epley`() = runTest {
        val params = RmParams(reps = Reps(11))
        val defaultAuthor = Author.Brzycki(params)
        
        val result = repository.getAuthor(params, autoFx = true, default = defaultAuthor)
        
        assertTrue(result is Author.Epley)
        assertEquals(params, result.params)
    }

    @Test
    fun `when autoFx is true and reps is exactly the limit, return Epley`() = runTest {
        val params = RmParams(reps = Reps(GetRmImpl.CONSISTENT_RESULT_LIMIT))
        val defaultAuthor = Author.Brzycki(params)
        
        val result = repository.getAuthor(params, autoFx = true, default = defaultAuthor)
        
        assertTrue(result is Author.Epley)
        assertEquals(params, result.params)
    }

    @Test
    fun `when autoFx is true and reps is above limit, return EpleyHighReps`() = runTest {
        val params = RmParams(reps = Reps(GetRmImpl.CONSISTENT_RESULT_LIMIT + 1))
        val defaultAuthor = Author.Brzycki(params)
        
        val result = repository.getAuthor(params, autoFx = true, default = defaultAuthor)
        
        assertTrue(result is Author.EpleyHighReps)
        assertEquals(params, result.params)
    }

    @Test
    fun `verify default author type is respected when autoFx is false`() = runTest {
        val params = RmParams(reps = Reps(20))
        val defaultAuthor = Author.Wathen(RmParams())
        
        val result = repository.getAuthor(params, autoFx = false, default = defaultAuthor)
        
        assertTrue(result is Author.Wathen)
        assertEquals(params, result.params)
    }
}
