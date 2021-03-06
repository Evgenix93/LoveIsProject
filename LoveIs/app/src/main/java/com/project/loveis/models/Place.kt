package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@JsonClass (generateAdapter = true)
data class Place(
    val id: Int,
    val address: String,
    val name: String,
    val photo: String
): Parcelable
