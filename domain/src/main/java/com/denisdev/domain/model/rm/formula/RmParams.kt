package com.denisdev.domain.model.rm.formula

import android.os.Parcelable
import com.denisdev.domain.model.units.Reps
import com.denisdev.domain.model.units.Weight
import com.denisdev.domain.model.units.WeightUnit
import kotlinx.parcelize.Parcelize

@Parcelize
data class RmParams(
    var weightUnit: WeightUnit = WeightUnit.Kg,
    var weight: Weight = Weight(weightUnit = weightUnit),
    var reps: Reps = Reps()
): Parcelable {

    constructor(weight: Weight, reps: Reps) : this() {
        this.weightUnit = weight.weightUnit
        this.weight = weight
        this.reps = reps
    }

    constructor(weight: String, reps: String, weightUnit: WeightUnit) : this() {
        this.weightUnit = weightUnit
        this.weight = Weight(format(weight), weightUnit)
        this.reps = Reps(reps.toIntOrNull() ?: 0)
    }

    private fun format(value: String): Float {
        return value.replace(",", ".").toFloatOrNull() ?: 0f
    }
}
