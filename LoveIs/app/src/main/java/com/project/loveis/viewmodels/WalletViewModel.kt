package com.project.loveis.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import com.project.loveis.repositories.WalletRepository
import kotlinx.coroutines.launch

class WalletViewModel(app: Application): AndroidViewModel(app) {
    private val walletRepository = WalletRepository()
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>()
    val state: LiveData<State>
        get() = stateLiveData

    fun addMoney (amount: Int) {
        viewModelScope.launch {
            val response = walletRepository.addMoney(amount)
            when(response?.code()){
                204 -> stateLiveData.postValue(State.SuccessState)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
            }
        }
    }

    fun getCurrentBalance(){
        viewModelScope.launch {
            val response = mainRepository.getCurrentUserInfo()
            when(response?.code()){
                 200 -> response.body()!!.wallet?.let{stateLiveData.postValue(State.LoadedSingleState(it))}
                 404 -> stateLiveData.postValue(State.ErrorState(404))
            }
        }
    }

    fun getMoney(amount: Int, card: String){
        viewModelScope.launch {
            val response = walletRepository.getMoney(amount, card)
            when(response?.code()){
                204 -> stateLiveData.postValue(State.SuccessState)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                409 -> stateLiveData.postValue(State.ErrorState(409))
            }
        }
    }

    fun getOperations(){
      viewModelScope.launch {
          val response = walletRepository.getOperations()
          when(response?.code()){
              200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
              400 -> stateLiveData.postValue(State.ErrorState(400))
          }
      }
    }
}