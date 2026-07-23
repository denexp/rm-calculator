package com.denisdev.rmcalculator.rmCalculator

import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.WeightUnit

data class RmUiData(
    val rm: Float = DEFAULT_RM,
    val authors: List<Author> = listOf(
        Author.Epley(),
        Author.OConner(),
        Author.Lombardi(),
        Author.Brzycki(),
        Author.Mayhew(),
        Author.Wathen(),
        Author.Lander(),
        Author.MCGlothin(),
        Author.EpleyHighReps(),
    ),
    val weight: String = "",
    val reps: String = "",
    val author: Author = DEFAULT_AUTHOR,
    val weightUnit: WeightUnit = DEFAULT_WEIGHT_UNIT,
    val autoFx: Boolean = true
) {
    companion object {
        const val DEFAULT_RM = 0.0f
        val DEFAULT_AUTHOR: Author = Author.Brzycki()
        val DEFAULT_WEIGHT_UNIT = WeightUnit.Kg
    }
}