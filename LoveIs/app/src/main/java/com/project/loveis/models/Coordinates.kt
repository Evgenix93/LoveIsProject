package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Coordinates(
    val latitude: String,
    val longitude: String,
    val city: String
): Parcelable
