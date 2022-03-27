package com.project.loveis.apis

import com.project.loveis.models.Video
import com.project.loveis.util.Gender
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.ByteArrayInputStream

interface VideoApi {

    @GET("review/")
    suspend fun getVideos(@Query("gender") gender: String): Response<List<Video>>

    @GET
    suspend fun downloadVideo(@Url url: String): ResponseBody
}