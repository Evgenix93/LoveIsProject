package com.project.loveis.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.project.loveis.models.Video
import com.project.loveis.singletones.Network
import com.project.loveis.singletones.ProfileInfo
import com.project.loveis.util.Gender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.File

class VideoRepository(private val context: Context) {

    suspend fun getVideos(): Response<List<Video>>? {
        return try {
            Network.videoApi.getVideos(ProfileInfo.currentUser!!.gender.name.lowercase())
        } catch (e: Throwable) {
            Log.e("MyDebug", "request error = ${e.message}")
            null
        }
    }

    suspend fun getVideoIntent(url: String): Intent? {
        return try {
            withContext(Dispatchers.IO) {
                val videoFile = File(context.externalCacheDir, "video.mp4")
                videoFile.outputStream().use { outputStream ->
                    Network.videoApi.downloadVideo(url).byteStream()
                        .use { inputStream ->
                            inputStream.copyTo(outputStream)
                        }
                }
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    type = "video/mp4"
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                intent.data = FileProvider.getUriForFile(
                    context,
                    context.applicationContext.packageName +
                            ".provider",
                    videoFile)
                intent
            }
        }catch (e: Throwable){
          Log.e("MyDebug", "intent error = ${e.message}")
            null
        }
    }
}