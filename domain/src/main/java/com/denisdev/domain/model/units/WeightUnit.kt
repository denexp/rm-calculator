package com.denisdev.domain.model.units

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WeightUnit : Parcelable {
    Kg,
    Lb
}