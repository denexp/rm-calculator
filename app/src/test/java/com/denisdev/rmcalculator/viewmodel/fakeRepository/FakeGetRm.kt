package com.denisdev.rmcalculator.viewmodel.fakeRepository

import com.denisdev.domain.model.rm.RM
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import com.denisdev.domain.usecases.UseCase
import com.denisdev.domain.usecases.rmcalculator.GetRm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeGetRm : GetRm {
    private val flow = MutableStateFlow<Result<RM>>(Result.success(RM(Author.Brzycki(), Weight(0f, WeightUnit.Kg))))
    suspend fun emit(value: Result<RM>) = flow.emit(value)
    override fun invoke(params: GetRm.Params?): Flow<Result<RM>> = flow
}