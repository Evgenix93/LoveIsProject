package com.project.loveis.repositories

import android.util.Log
import com.project.loveis.models.*
import com.project.loveis.singletones.Network
import com.project.loveis.util.MeetingStatus
import retrofit2.Response

class LoveIsEventIsRepository {
    private val loveIsApi = Network.loveIsEventIsApi

    suspend fun getLoveIsMeetings(page: Int, size: Int, type: MeetingFilterType): Response<LoveIsListResult>?{
        return try {
            loveIsApi.getLoveIsMeetings(page, size, type.value)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun getEventIsMeetings(page: Int, size: Int, type: MeetingFilterType): Response<EventIsListResult>?{
        return try {
            loveIsApi.getEventIsMeetings(page, size, type.value)
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

    suspend fun createLoveIs(
        type: Int,
        place: Int,
        date: String,
        telegramUrl:String,
        whatsAppUrl:String,
        userId:Long
    ): Response<LoveIs>? {
        return  try {
            Network.loveIsEventIsApi.createLoveIs(
                CreateLoveIsRequest(
                date,
                telegramUrl.ifEmpty { null },
                whatsAppUrl.ifEmpty { null },
                type,
                place,
                userId
            )
            )
        }catch (e: Exception){
            Log.e("Debug", "error ${e.message}")
            null
        }
    }

    suspend fun createEventIs(
        type: Int,
        place: Int,
        price: Int,
        date: String,
        personCount: Int,
        telegramUrl:String,
        whatsAppUrl:String
    ): Response<EventIs>?{
        return try {
            Network.loveIsEventIsApi.createEventIs(
                CreateEventIsRequest(
                date,
                telegramUrl.ifEmpty{null},
                whatsAppUrl.ifEmpty { null },
                type,
                place,
                price,
                personCount,
                "test event"
            )
            )
        }catch (e: Throwable){
            Log.e("MyDebug", "error = ${e.message}")
            null
        }
    }




}