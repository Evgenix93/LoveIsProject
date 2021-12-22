package com.project.loveis.apis

import com.project.loveis.models.Place
import com.project.loveis.models.PlaceListResult
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PlaceApi {

    @POST("place/")
    @Multipart
    suspend fun createPlace(
        @Part("name") name: String,
        @Part("address") address: String,
        @Part photo: MultipartBody.Part
    ): Response<Unit>

    @GET("place/")
    suspend fun getPlaces(): Response<PlaceListResult>?
}