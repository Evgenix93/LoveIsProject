package com.project.loveis.models

sealed class Message{
   data class MyMessage(val text: String): Message()
   data class MyMediaMessage(val text: String, val url: String): Message()
   data class CompanionMessage(val text: String): Message()
   data class CompanionMediaMessage(val text: String, val url: String): Message()
}
