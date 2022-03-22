package com.project.loveis.repositories

import android.util.Log
import com.project.loveis.models.LoveIs
import com.project.loveis.models.LoveIsListResult
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.singletones.Network
import com.project.loveis.util.MeetingStatus
import retrofit2.Response
import java.io.IOException

class LoveIsRepository {
    private val loveIsApi = Network.loveIsApi

    suspend fun getLoveIsMeetings(page: Int, size: Int, type: MeetingFilterType): Response<LoveIsListResult>?{
        return try {
            loveIsApi.getLoveIsMeetings(page, size, type.value)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun completeLoveIs(id: Long): Response<LoveIs>?{
        return try {
            loveIsApi.completeLoveIs(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun changeLoveIsStatus(id: Long, status: MeetingStatus): Response<LoveIs>?{
        return try {
            loveIsApi.changeLoveIsStatus(id, status.value)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun acceptLoveIs(id: Long): Response<LoveIs>?{
        return try {
            loveIsApi.acceptLoveIs(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }


}