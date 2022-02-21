package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Image(
    var number: Int?,
    val uuid: String,
    val url: String
): Parcelable
