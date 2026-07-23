package com.denisdev.domain.model.rm.formula

import com.denisdev.domain.model.Formula
import com.denisdev.domain.model.rm.author.Author
import com.denisdev.domain.model.units.Weight

interface RmFormula: Formula<Weight> {
    val name: String
    val params: RmParams
    override fun calculate(): Weight
}