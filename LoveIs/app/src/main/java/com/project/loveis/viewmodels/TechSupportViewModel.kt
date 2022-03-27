package com.project.loveis.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.loveis.State
import com.project.loveis.repositories.TechSupportRepository
import kotlinx.coroutines.launch

class TechSupportViewModel: ViewModel() {
    private val repository = TechSupportRepository()
    private val stateLiveData = MutableLiveData<State>()
    val state: LiveData<State>
        get() = stateLiveData

    fun sendRequest(text: String){
      viewModelScope.launch{
          when(repository.sendRequest(text)?.code()){
              200 -> stateLiveData.postValue(State.SuccessState)
              400 -> stateLiveData.postValue(State.ErrorState(400))
              null -> stateLiveData.postValue(State.ErrorState(0))
          }
      }
    }
}