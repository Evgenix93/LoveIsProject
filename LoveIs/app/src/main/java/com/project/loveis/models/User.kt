package com.project.loveis.models

import android.os.Parcelable
import com.project.loveis.util.Gender
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class User(
    val id: Long?,
    val groups: List<Group>,
    val coordinates: Coordinates,
    val verified: Boolean,
    val password: String?,
    val username: String,
    val name: String,
    val phone: String,
    val wallet: Wallet?,
    val birthday: String,
    val about: String,
    val gender: Gender,
    val photo: String,
    var images: List<Image>,
    val subscription: Subsription?,
    @Json(name = "verification_status")
    val verificationStatus: String?
): Parcelable{
    companion object{
        const val VERIFY_STATUS_PENDING = "pending"
    }
}


