package com.denisdev.domain.model.rm.author

import android.os.Parcelable
import com.denisdev.domain.model.rm.formula.RmFormula
import com.denisdev.domain.model.rm.formula.RmParams
import com.denisdev.domain.model.units.Weight
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import kotlin.math.E
import kotlin.math.pow

sealed class Author: RmFormula, Parcelable {
    @Parcelize
    data class Brzycki(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Brzycki"
        override fun calculate() =
            Weight((params.weight.value / (1.0278 - (0.0278 * params.reps.value))).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class Epley(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Epley"
        override fun calculate() = Weight((params.weight.value * ( 1 + (0.033 * params.reps.value))).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class EpleyHighReps(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "EpleyHighReps"
        override fun calculate() = Weight((params.weight.value * ( 1 + (0.03 * params.reps.value))).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class Lander(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Lander"
        override fun calculate() =
            Weight((100 * params.weight.value / (101.3F - (2.67123 * params.reps.value))).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class Lombardi(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Lombardi"
        override fun calculate() =
            Weight((params.weight.value * params.reps.value.toDouble().pow(0.10)).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class Mayhew(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Mayhew"
        override fun calculate() =
            Weight((100 * params.weight.value / (52.2 + 41.9 * E.pow(-0.055 * params.reps.value))).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class MCGlothin(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "MCGlothin"
        override fun calculate() =
            Weight((params.weight.value / (1.0278 - 0.0278 * params.reps.value)).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class OConner(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "OConner"
        override fun calculate() = Weight((params.weight.value * (1 + 0.025 * params.reps.value)).toFloat(), params.weightUnit)
    }
    @Parcelize
    data class Wathen(override val params: RmParams = RmParams()): Author() {
        @IgnoredOnParcel
        override val name = "Wathen"
        override fun calculate() =
            Weight((100 * params.weight.value / (48.8 + 53.8 * E.pow(-0.075 * params.reps.value))).toFloat(), params.weightUnit)
    }
    companion object {
        fun build(name: String, params: RmParams): Author {
            return when (name) {
                Brzycki().name -> Brzycki(params)
                Epley().name -> Epley(params)
                EpleyHighReps().name -> EpleyHighReps(params)
                Lander().name -> Lander(params)
                Lombardi().name -> Lombardi(params)
                Mayhew().name -> Mayhew(params)
                MCGlothin().name -> MCGlothin(params)
                OConner().name -> OConner(params)
                Wathen().name -> Wathen(params)
                else -> throw IllegalArgumentException("Unknown author: $name")
            }
        }
    }
}