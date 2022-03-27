package com.project.loveis.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.loveis.State
import com.project.loveis.models.SubsriptionRequestData
import com.project.loveis.repositories.SubsriptionRepository
import kotlinx.coroutines.launch

class SubsriptionViewModel: ViewModel() {
    private val stateLiveData = MutableLiveData<State>()
    private val subsriptionRepository = SubsriptionRepository()

    val state: LiveData<State>
    get() = stateLiveData

    fun confirmSubsription(value: Int, date: String){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = subsriptionRepository.confirmSubsription(SubsriptionRequestData(value, date))
            when (response?.code()) {
                204 -> {
                    stateLiveData.postValue(State.SuccessState)
                }

                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }
}