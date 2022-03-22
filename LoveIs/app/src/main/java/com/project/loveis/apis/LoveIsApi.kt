package com.project.loveis.apis

import com.project.loveis.models.CreateEventIsRequest
import com.project.loveis.models.CreateLoveIsRequest
import com.project.loveis.models.EventIs
import com.project.loveis.models.LoveIs
import com.project.loveis.models.LoveIsListResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.*

interface LoveIsApi {

    @POST("meeting/")
    suspend fun createLoveIs(@Body loveIs: CreateLoveIsRequest): Response<LoveIs>

    @GET("meetings/types")
    suspend fun getTypes()

    @GET("invitations/meetings")
    suspend fun getLoveIsMeetings(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String
    ): Response<LoveIsListResult>

    @PATCH("meeting/status/complete/{meeting_id}")
    suspend fun completeLoveIs(@Path("meeting_id") id: Long): Response<LoveIs>

    @PATCH("meeting/status/change/{meeting_id}")
    suspend fun changeLoveIsStatus(@Path("meeting_id") id: Long, @Query("status") status: String): Response<LoveIs>

    @PATCH("meeting/status/change/{meeting_id}/accept")
    suspend fun acceptLoveIs(@Path("meeting_id") id: Long): Response<LoveIs>

    @POST("event/")
    suspend fun createEventIs(@Body eventIs: CreateEventIsRequest): Response<EventIs>
}