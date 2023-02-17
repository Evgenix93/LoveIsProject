package com.project.loveis.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.User
import com.project.loveis.repositories.AuthRepository
import com.project.loveis.repositories.ChatRepository
import com.project.loveis.repositories.MainRepository
import kotlinx.coroutines.launch

class ChatViewModel(application: Application): AndroidViewModel(application) {
    private val chatRepository = ChatRepository(application)
    private val mainRepository = MainRepository(application)
    private val authRepository = AuthRepository(application)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    val state: LiveData<State>
        get() = stateLiveData
    private var attachmentUris: List<Uri>? = null
    private var currentUser: User? = null

    fun sendMessage(message: String, userId: Long){
        stateLiveData.postValue(State.LoadingState)
      viewModelScope.launch{
          if(message.isBlank() && (attachmentUris == null || attachmentUris?.isEmpty() == true)){
              stateLiveData.postValue(State.ErrorMessageState("Введите сообщение"))
              return@launch
          }
          var isPhotoOk = attachmentUris?.all { authRepository.checkFileSize(it) }
          if(isPhotoOk == false){
              stateLiveData.postValue(State.ErrorMessageState("один или несколько файлов превышают допустимый размер в 1мб"))
              return@launch
          }

          val response = chatRepository.sendMessage(message.ifEmpty { null }, attachmentUris, userId)
          when(response?.code()){
              200 -> stateLiveData.postValue(State.SuccessState)
              400 -> stateLiveData.postValue(State.ErrorState(400))
              404 -> stateLiveData.postValue(State.ErrorState(404))
          }
          attachmentUris = null

      }
    }

    fun getMessages(userId: Long){
        stateLiveData.postValue(State.LoadingState)
       viewModelScope.launch {
         val response = chatRepository.getMessages(userId)
         when(response?.code()){
           200 -> {
               stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
             //  val message = response.body()!!.list?.last()
             //  if(message?.unread == false)
              //  chatRepository.readMessage(userId, message.id)
           }
             400 -> stateLiveData.postValue(State.ErrorState(400))
             404 -> stateLiveData.postValue(State.ErrorState(404))
         }
       }
    }



    fun setAttachmentUri(uris: List<Uri>){
        attachmentUris = uris
    }

    fun getDialogs(){
        viewModelScope.launch{
          val response = chatRepository.getDialogs()
            when(response?.code()){
                200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                400 -> stateLiveData.postValue(State.ErrorState(400))
                404 -> stateLiveData.postValue(State.ErrorState(404))
            }
        }
    }

    fun setCurrentUser(){
       currentUser = mainRepository.getCurrentUser()
    }

    fun getCurrentUser(): User?{
        return currentUser
    }
}