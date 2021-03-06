package com.project.loveis.apis

import com.project.loveis.models.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.*

interface LoveIsEventIsApi {

    @POST("meeting/")
    suspend fun createLoveIs(@Body loveIs: CreateLoveIsRequest): Response<LoveIs>

    @POST("meeting/")
    suspend fun createLoveIsGetError(@Body loveIs: CreateLoveIsRequest): Response<ResponseError>

    @POST("meeting/")
    suspend fun createLoveIsGetErrors(@Body loveIs: CreateLoveIsRequest): Response<ResponseErrorsMap>




    @GET("meetings/types")
    suspend fun getTypes(): Response<TypesWrapper>

    @GET("invitations/meetings")
    suspend fun getLoveIsMeetings(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String
    ): Response<LoveIsListResult>

    @GET("invitations/meetings")
    suspend fun getLoveIsMeetingsGetError(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String
    ): Response<ResponseError>

    @GET("invitations/meetings")
    suspend fun getLoveIsMeetingsGetErrors(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String
    ): Response<ResponseErrorsMap>



    @GET("meeting/")
    suspend fun getAllLoveIsMeetingsForCurrentUser(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<LoveIsListResult>

    @PATCH("meeting/status/complete/{meeting_id}")
    suspend fun completeLoveIs(@Path("meeting_id") id: Long): Response<LoveIs>

    @PATCH("meeting/status/change/{meeting_id}")
    suspend fun changeLoveIsStatus(
        @Path("meeting_id") id: Long,
        @Query("status") status: String
    ): Response<LoveIs>

    @PATCH("meeting/status/change/{meeting_id}/accept")
    suspend fun acceptLoveIs(@Path("meeting_id") id: Long): Response<LoveIs>

    @POST("event/")
    suspend fun createEventIs(@Body eventIs: CreateEventIsRequest): Response<EventIs>

    @GET("invitations/events")
    suspend fun getEventIsMeetings(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("type") type: String
    ): Response<EventIsListResult>

    @GET("event/")
    suspend fun getMyEventIsMeetings(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("status") type: String
    ): Response<EventIsListResult>

    @GET("event/participants/{event_id}")
    suspend fun getEventMembers(
        @Path("event_id") eventId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<EventMembersListResult>

    @PATCH("event/complete/{event_id}")
    suspend fun completeEventIs(@Path("event_id") id: Long): Response<EventIs>

    @POST("event/{event_id}")
    suspend fun joinEventIs(@Path("event_id") id: Long): Response<EventIs>

    @DELETE("event/{event_id}/participants/{user_id}/")
    suspend fun removeParticipantFromEventIs(@Path("event_id") eventId: Long, @Path("user_id") userId: Long): Response<Unit>

    @GET("event/{event_id}")
    suspend fun getEventIsById(@Path("event_id") id: Long): Response<EventIs>

    @GET("meeting/{meeting_id}")
    suspend fun getLoveIsById(@Path("meeting_id") id: Long): Response<LoveIs>

}