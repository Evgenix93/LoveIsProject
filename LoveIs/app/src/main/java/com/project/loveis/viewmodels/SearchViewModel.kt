package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(app: Application): AndroidViewModel(app) {
    private val repository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    private var _age = 18
    private var _gender = "female"
    val state: LiveData<State>
    get() = stateLiveData

    fun searchUsers(age: Int = _age, gender: String = _gender){
        stateLiveData.postValue(State.LoadingState)
        _age = age
        _gender = gender
        viewModelScope.launch {
            val response = repository.searchUsers(age, gender)
            if (response == null)
                stateLiveData.postValue(State.ErrorState(0))
            else {
                when (response.code()){
                    200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                    401 -> stateLiveData.postValue(State.ErrorMessageState("Учетные данные не были предоставлены"))
                    else -> stateLiveData.postValue(State.ErrorMessageState("searchUsers код ошибки: ${response.code()}"))
                }
            }
        }
    }

    fun collectFlow(flow: Flow<Int>){
        flow.debounce(1000)
            .mapLatest {
                Log.d("Debug", "mapLatest = $it")
                searchUsers(it)
            }
            .launchIn(viewModelScope)
    }

    fun getCurrentUser(){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = repository.getCurrentUserInfo()
            if(response != null) {
                when(response.code()){
                    200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                    404 -> stateLiveData.postValue(State.ErrorState(404))
                }
            }else{
                stateLiveData.postValue(State.ErrorState(0))
            }
        }

    }
    fun clearState(){
        stateLiveData.postValue(State.StartState)
    }

    fun shareUser(userId: Long){
        stateLiveData.postValue(State.LoadedIntent(repository.getShareUserIntent(userId)))
    }

    fun getUserById(id: Long){
        viewModelScope.launch {
            Log.d("mylog", "getUserById id: $id")
            stateLiveData.postValue(State.LoadingState)
            val response = repository.getUserById(id)
            when(response?.code()){
                200 -> stateLiveData.postValue(State.SharedUserLoaded(response.body()!!))
                null -> stateLiveData.postValue(State.ErrorState(0))
                500 -> stateLiveData.postValue(State.ErrorState(2))
                404 -> stateLiveData.postValue(State.ErrorMessageState("Пользователь не найден"))
                else -> stateLiveData.postValue(State.ErrorMessageState("getUserById код ошибки: ${response.code()}"))

            }

        }
    }
}