package com.project.loveis.apis

import com.project.loveis.models.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {

    @GET("user/")
    suspend fun searchUsers(@Query("age_from")ageFrom: Int, @Query("age_to")ageTo: Int,  @Query("gender")gender: String): Response<SearchResult>
}