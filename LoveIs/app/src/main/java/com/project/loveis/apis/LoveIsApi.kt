package com.project.loveis.apis

import com.project.loveis.models.CreateEventIsRequest
import com.project.loveis.models.CreateLoveIsRequest
import com.project.loveis.models.EventIs
import com.project.loveis.models.LoveIs
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoveIsApi {

    @POST("meeting/")
    suspend fun createLoveIs(@Body loveIs: CreateLoveIsRequest): Response<LoveIs>

    @GET("meetings/types")
    suspend fun getTypes()

    @POST("event/")
    suspend fun createEventIs(@Body eventIs: CreateEventIsRequest): Response<EventIs>
}