package com.project.loveis.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.ChatRepository
import com.project.loveis.singletones.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.javaConstructor

class ChatViewModel(application: Application): AndroidViewModel(application) {
    private val repository = ChatRepository(application)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    val state: LiveData<State>
        get() = stateLiveData
    private var attachmentUri: Uri? = null

    fun sendMessage(message: String, userId: Long){
        stateLiveData.postValue(State.LoadingState)
      viewModelScope.launch{
          val response = repository.sendMessage(message, attachmentUri, userId)
          when(response?.code()){
              204 -> stateLiveData.postValue(State.SuccessState)
              400 -> stateLiveData.postValue(State.ErrorState(400))
              404 -> stateLiveData.postValue(State.ErrorState(404))
          }
      }
    }

    fun getMessages(userId: Long){
        stateLiveData.postValue(State.LoadingState)
       viewModelScope.launch {
         val response = repository.getMessages(userId)
         when(response?.code()){
           200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
             400 -> stateLiveData.postValue(State.ErrorState(400))
             404 -> stateLiveData.postValue(State.ErrorState(404))
         }
       }
    }

    fun setAttachmentUri(uri: Uri){
        attachmentUri = uri
    }

    fun getDialogs(){
        viewModelScope.launch{
          val response = repository.getDialogs()
            when(response?.code()){
                200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                400 -> stateLiveData.postValue(State.ErrorState(400))
                404 -> stateLiveData.postValue(State.ErrorState(404))
            }
        }
    }

}