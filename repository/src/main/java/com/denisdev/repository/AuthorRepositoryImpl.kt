package com.denisdev.repository

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.repository.AuthorRepository
import com.denisdev.domain.usecases.rmcalculator.GetRmImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthorRepositoryImpl(private val dispatcher: CoroutineDispatcher = Dispatchers.IO): AuthorRepository {
    override suspend fun getAuthor(rmParams: RmParams, autoFx: Boolean, default: Author): Author = withContext(dispatcher) {
        if (0 == rmParams.reps.value)
            return@withContext Author.build(default.name, rmParams.copy(weight = Weight(0f,
                        rmParams.weightUnit
                    )
                )
            )

        if (!autoFx)
            return@withContext Author.build(default.name, rmParams)

        if ((1..10).contains(rmParams.reps.value))
            return@withContext Author.Brzycki(rmParams)

        if ((11..GetRmImpl.CONSISTENT_RESULT_LIMIT).contains(rmParams.reps.value))
            return@withContext Author.Epley(rmParams)

        return@withContext Author.EpleyHighReps(rmParams)
    }
}