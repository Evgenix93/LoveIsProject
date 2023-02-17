package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.Dialog
import com.project.loveis.repositories.ChatRepository
import com.project.loveis.repositories.LoveIsEventIsRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(app: Application): AndroidViewModel(app) {
    private val stateLiveData = MutableLiveData<State>()
    private val chatRepository = ChatRepository(app)

    val state: LiveData<State>
    get() = stateLiveData

    fun getMessages(userId: Long) {
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val response = chatRepository.getMessages(userId)
            when (response?.code()) {
                200 -> {stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                // val dialog = response.body() as Dialog
                 // if(dialog.unread == "0")
                  //    stateLiveData.postValue(State.UnreadMessageState)
                        }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                404 -> stateLiveData.postValue(State.ErrorState(404))
            }

        }
    }




    private val loveIsRepository = LoveIsEventIsRepository()



    fun getLoveIsById(id: Long) {
        Log.d("MyDebug", "getLoveIs id = $id")
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getLoveIsById(id)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.LoveIsSingleMeetingLoadedState(response.body()!!))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }
}