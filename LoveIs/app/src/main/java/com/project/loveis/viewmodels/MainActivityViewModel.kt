package com.project.loveis.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.ChatRepository
import kotlinx.coroutines.launch

class MainActivityViewModel(app: Application): AndroidViewModel(app) {
    private val stateLiveData = MutableLiveData<State>()
    private val chatRepository = ChatRepository(app)

    val state: LiveData<State>
    get() = stateLiveData

    fun getMessages(userId: Long){
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val response = chatRepository.getMessages(userId)
            when(response?.code()){
                200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                400 -> stateLiveData.postValue(State.ErrorState(400))
                404 -> stateLiveData.postValue(State.ErrorState(404))
            }
        }
    }
}