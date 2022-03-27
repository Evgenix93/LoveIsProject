package com.project.loveis.repositories

import android.util.Log
import com.project.loveis.models.SubsriptionRequestData
import com.project.loveis.singletones.Network
import retrofit2.Response

class SubsriptionRepository {
    val subsriptionApi = Network.sabsriptionApi

    suspend fun confirmSubsription(data: SubsriptionRequestData): Response<Unit>?{
        return try {
            subsriptionApi.confirmSubsription(data)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }
}