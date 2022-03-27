package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Subsription(
    val value: Int,
    val until: String
): Parcelable
