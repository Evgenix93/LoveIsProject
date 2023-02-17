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

   suspend fun sendMessage(message: String?, attachments: List<Uri>?, userId: Long): Response<Any>? {
       Log.d("MyDebug", "sendMessage")
       return try {
           withContext(Dispatchers.IO) {
               val attachmentsParts = attachments?.map {
                   //val attachmentPart = if(attachments != null){

                   val file = File(
                       context.cacheDir,
                       "attachment_${it.lastPathSegment}.${
                           context.contentResolver.getType(it)?.substringAfterLast("/")
                       }"
                   )
                   context.contentResolver.openInputStream(it).use { inputStream ->
                       file.outputStream().use { outputStream ->
                           inputStream?.copyTo(outputStream)
                       }
                   }
                   MultipartBody.Part.createFormData(
                       "attachment",
                       file.name,
                       file.asRequestBody()
                   )
               //}else null
           }

               Network.chatApi.sendMessage(
                   userId,
                   if(message == null) null else MultipartBody.Part.createFormData("content", message),
                   if(attachmentsParts?.size ?: 0 > 0) attachmentsParts!![0] else null,
                   if(attachmentsParts?.size ?: 0 > 1) attachmentsParts!![1] else null,
                   if(attachmentsParts?.size ?: 0 > 2) attachmentsParts!![2] else null,
                   if(attachmentsParts?.size ?: 0 > 3) attachmentsParts!![3] else null,
                   if(attachmentsParts?.size ?: 0 > 4) attachmentsParts!![4] else null,
                   if(attachmentsParts?.size ?: 0 > 5) attachmentsParts!![5] else null,
                   if(attachmentsParts?.size ?: 0 > 6) attachmentsParts!![6] else null,
                   if(attachmentsParts?.size ?: 0 > 7) attachmentsParts!![7] else null,
                   if(attachmentsParts?.size ?: 0 > 8) attachmentsParts!![8] else null,
                   if(attachmentsParts?.size ?: 0 > 9) attachmentsParts!![9] else null

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

    suspend fun readMessage(userId: Long, messageId: Long){
        Network.chatApi.readMessage(userId, messageId)
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