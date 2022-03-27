package com.project.loveis.repositories


import android.content.Context
import android.net.Uri
import android.util.Log
import com.project.loveis.models.Dialog
import com.project.loveis.models.DialogsWrapper
import com.project.loveis.singletones.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class ChatRepository(private val context: Context) {

   suspend fun sendMessage(message: String, attachment: Uri?, userId: Long): Response<Any>? {
       Log.d("MyDebug", "sendMessage")
       return try {
           withContext(Dispatchers.IO) {
                   val attachmentPart = if(attachment != null){
                       val file = File(context.cacheDir, "attachment.${context.contentResolver.getType(attachment)?.substringAfterLast("/")}")
                       context.contentResolver.openInputStream(attachment).use { inputStream ->
                           file.outputStream().use {
                               inputStream?.copyTo(it)
                           }
                       }
                       MultipartBody.Part.createFormData(
                           "attachment",
                           file.name,
                           file.asRequestBody()
                       )
                   }else null

               Network.chatApi.sendMessage(
                   userId,
                   MultipartBody.Part.createFormData("content", message),
                   attachmentPart
               )
           }
           } catch (e: Throwable){
               Log.e("mydebug", "error message = ${e.message}")
               null
           }
       }

    suspend fun getMessages(userId: Long): Response<Dialog>?{
      return try {
          Network.chatApi.getMessages(userId)
      }catch (e: Throwable){
          Log.e("MyDebug", "error message = ${e.message}")
          null
      }
    }

    suspend fun getDialogs(): Response<DialogsWrapper>?{
      return try {
          Network.chatApi.getDialogs()
      }catch (e: Throwable){
          Log.e("MyDebug", "error message = ${e.message}")
          null
      }
    }
}