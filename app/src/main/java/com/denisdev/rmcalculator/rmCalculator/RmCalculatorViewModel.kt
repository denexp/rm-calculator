package com.denisdev.rmcalculator.rmCalculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisdev.domain.usecases.rmcalculator.GetRm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RmCalculatorViewModel @Inject constructor(
    private val rmUseCase: GetRm,
): ViewModel() {
    private val _data: MutableStateFlow<RmUiData> = MutableStateFlow(RmUiData())
    val data = _data.asStateFlow()

    fun getRm(params: GetRm.Params) {
        viewModelScope.launch {
            rmUseCase(params).collect { result ->
                result.fold(
                    onSuccess = {
                        _data.update {
                            it.copy(
                                rm = result.getOrThrow().weight.value,
                                author = result.getOrThrow().author,
                                reps = params.reps,
                                weight = params.weight,
                                weightUnit = params.weightUnit,
                            )
                        }
                    },
                    onFailure = {
                        _data.update { it.copy(rm = RmUiData.DEFAULT_RM) }
                    }
                )
            }
        }
    }
}

