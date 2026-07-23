package com.denisdev.rmcalculator.rmCalculator

import com.denisdev.domain.usecases.rmcalculator.GetRm

sealed class RmCalculatorEvent {
    data class UpdateRm(val params: GetRm.Params) : RmCalculatorEvent()
}