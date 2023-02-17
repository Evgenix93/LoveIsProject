package com.project.loveis.repositories

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager

import android.content.Context
import android.content.Intent

import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.project.loveis.models.*
import com.project.loveis.singletones.Network
import com.project.loveis.util.MeetingStatus
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException


class LoveIsEventIsRepository {
    private val loveIsApi = Network.loveIsEventIsApi

    suspend fun getLoveIsMeetings(page: Int, size: Int, type: MeetingFilterType): Response<LoveIsListResult>?{
        return try {
            loveIsApi.getLoveIsMeetings(page, size, type.value)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            if(e is IOException) null
            else{
                try {
                    val response = loveIsApi.getLoveIsMeetingsGetError(page, size, type.value)
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = loveIsApi.getLoveIsMeetingsGetErrors(page, size, type.value)
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
        }
    }

    suspend fun getCurrentUserLoveIsMeetings(page: Int, size: Int): Response<LoveIsListResult>?{
        return try {
            loveIsApi.getAllLoveIsMeetingsForCurrentUser(page, size)
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

    suspend fun completeEventIs(id: Long): Response<EventIs>?{
        return try {
            loveIsApi.completeEventIs(id)
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
        Log.d("MyTag", "createLoveIs")
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
            Log.e("MyTag", "error ${e.message}")
            if(e is IOException) {
                null
            }else{
                try {
                    val response = loveIsApi.createLoveIsGetError(CreateLoveIsRequest(
                        date,
                        telegramUrl.ifEmpty { null },
                        whatsAppUrl.ifEmpty { null },
                        type,
                        place,
                        userId
                    )
                    )
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = loveIsApi.createLoveIsGetErrors(CreateLoveIsRequest(
                        date,
                        telegramUrl.ifEmpty { null },
                        whatsAppUrl.ifEmpty { null },
                        type,
                        place,
                        userId
                    )
                    )
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
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

    suspend fun getEventMembers(page: Int, size: Int, eventId: Long): Response<EventMembersListResult>?{
        return try {
            loveIsApi.getEventMembers(eventId, page, size)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun joinEventIs(id: Long): Response<EventIs>?{
        return try {
            loveIsApi.joinEventIs(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun removeParticipantFromEventIs(eventId: Long, userId: Long): Response<Unit>?{
        return try {
            loveIsApi.removeParticipantFromEventIs(eventId, userId)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    fun getShareEventIntent(id: Long, context: Context): Intent{
        val url =  "https://loveis.scratch.studio/eventis/$id"
        val clipboard: ClipboardManager? =
            getSystemService(context, ClipboardManager::class.java )
        val clip = ClipData.newPlainText("", url)
        clipboard?.setPrimaryClip(clip)
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
    }

    fun getShareLoveIsIntent(id: Long, context: Context): Intent{
        val url =  "https://loveis.scratch.studio/loveis/$id"
        val clipboard: ClipboardManager? =
            getSystemService(context, ClipboardManager::class.java )
        val clip = ClipData.newPlainText("", url)
        clipboard?.setPrimaryClip(clip)
        //data = Uri.parse("https://loveis/meeting/$id")
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }



    }


    suspend fun getEventIsById(id: Long): Response<EventIs>?{
        return try {
            loveIsApi.getEventIsById(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun getLoveIsById(id: Long): Response<LoveIs>?{
        return try {
            loveIsApi.getLoveIsById(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            null
        }
    }

    suspend fun getTypes(): Response<TypesWrapper>?{
        return try {
            loveIsApi.getTypes()
        }catch(e: Throwable){
            Log.e("MyDebug", e.message.orEmpty())
            null
        }
    }
}