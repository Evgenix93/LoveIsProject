package com.project.loveis.models

sealed class Message{
   data class MyMessage(val text: String): Message()
   data class MyMediaMessage(val text: String?, val urls: List<String>): Message()
   data class CompanionMessage(val text: String): Message()
   data class CompanionMediaMessage(val text: String?, val urls: List<String>): Message()
}
