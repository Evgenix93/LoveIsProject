package com.project.loveis.apis

import com.project.loveis.models.*
import okhttp3.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @GET("phone/")
    suspend fun verifyPhone(@Query("phone") phone: String, @Query("debug") debug: Boolean): retrofit2.Response<Unit>

    @POST("phone/")
    suspend fun submitPhone(@Body phoneWithCode: PhoneWithCode): retrofit2.Response<Unit>

    @POST("token/")
    suspend fun getToken(@Body tokenData: TokenData): retrofit2.Response<TokenResponse>

    @POST("token/")
    suspend fun getTokenGetError(@Body tokenData: TokenData): retrofit2.Response<ResponseError>

    @POST("token/")
    suspend fun getTokenGetErrors(@Body tokenData: TokenData): retrofit2.Response<ResponseErrorsMap>



    @POST("user/signup/")
    @Multipart
    suspend fun registerNewUser(@PartMap info: Map<String, @JvmSuppressWildcards RequestBody>, @Part photo: MultipartBody.Part?): retrofit2.Response<Unit>

    @PATCH("user/")
    suspend fun changePhone(@Body newPhone: Phone): Response<Unit>

    @GET("api/device/gcm/{registration_id}/")
    suspend fun getGcmDevice(@Path("registration_id") token: String): Response<GcmDevice>

    @POST("api/device/gcm/")
    suspend fun createGcmDevice(@Body device: GcmDevice): Response<GcmDevice>

    @GET("api/device/gcm/")
    suspend fun getGcmDevices(): Response<Unit>


}