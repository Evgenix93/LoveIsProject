package com.project.loveis.apis


import com.project.loveis.models.Coordinates
import com.project.loveis.models.ResponseError
import com.project.loveis.models.ResponseErrorsMap
import com.project.loveis.models.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("user/{user_id}/")
    suspend fun getUserInfoById(@Path("user_id") id: Long): Response<User>

    @GET("user/{user_id}/")
    suspend fun getUserInfoByIdGetError(@Path("user_id") id: Long): Response<ResponseError>

    @GET("user/{user_id}/")
    suspend fun getUserInfoByIdGetErrors(@Path("user_id") id: Long): Response<ResponseErrorsMap>



    @POST("user/")
    @Multipart
    suspend fun updateUserPhoto(@Part photo: MultipartBody.Part): Response<User>

    @PUT("user/images/")
    @Multipart
    suspend fun updateAdditionalPhoto(@Part url: MultipartBody.Part): Response<User>

    @DELETE("user/images/{uuid}/")
    suspend fun deleteAdditionalPhoto(@Path("uuid") uuid: String): Response<User>

    @POST("user/")
    @Multipart
    suspend fun updateUserInfo(@Part name: MultipartBody.Part, @Part about: MultipartBody.Part ): Response<User>




    @Multipart
    @POST("user/verification/")
    suspend fun verify(@Part photoPart: MultipartBody.Part): Response<Unit>

    @POST("user/location/")
    suspend fun updateCoordinates(@Body coordinates: Coordinates): Response<Coordinates>

    @POST("user/location/")
    suspend fun updateCoordinatesGetError(@Body coordinates: Coordinates): Response<ResponseError>

    @POST("user/location/")
    suspend fun updateCoordinatesGetErrors(@Body coordinates: Coordinates): Response<ResponseErrorsMap>



}