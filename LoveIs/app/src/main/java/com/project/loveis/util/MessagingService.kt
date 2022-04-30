package com.project.loveis.util

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.project.loveis.models.PushModelJsonAdapter
import com.squareup.moshi.Moshi

class MessagingService: FirebaseMessagingService() {
    private lateinit var localBroadCaster: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()
        localBroadCaster = LocalBroadcastManager.getInstance(baseContext)

    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("mylog", "remote message ${message.data["meta"]}")
        val push = PushModelJsonAdapter(Moshi.Builder().build()).fromJson(message.data["meta"]!!)
        Log.d("mylog", "push $push")
        val intent = Intent(PUSH_INTENT).apply {
            putExtra(PUSH_DATA, push)
        }
        localBroadCaster.sendBroadcast(intent)


    }

    companion object{
        const val PUSH_INTENT = "push_intent"
        const val PUSH_DATA = "push_data"
    }
}