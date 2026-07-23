package com.denisdev.domain.model.units

import android.os.Parcelable
import java.security.InvalidParameterException
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reps(
    val value: Int = 0
): Parcelable {
    init {
        if (value < 0)
            throw InvalidParameterException()
    }
}