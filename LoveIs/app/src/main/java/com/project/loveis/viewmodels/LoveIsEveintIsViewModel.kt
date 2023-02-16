package com.project.loveis.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.User
import com.project.loveis.repositories.AuthRepository
import com.project.loveis.repositories.LoveIsEventIsRepository
import com.project.loveis.repositories.MainRepository
import com.project.loveis.util.MeetingStatus
import kotlinx.coroutines.launch
import java.util.*

class LoveIsEveintIsViewModel(app: Application) : AndroidViewModel(app) {
    private val loveIsRepository = LoveIsEventIsRepository()
    private val authRepository = AuthRepository(app)
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    private lateinit var currentUser: User

    val state: LiveData<State>
        get() = stateLiveData

    fun getLoveIsMeetings(page: Int = 1, size: Int = 25, type: MeetingFilterType) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getLoveIsMeetings(page, size, type)
            when (response?.code()) {
                200 -> {
                    val loveIsList = when (type) {
                        MeetingFilterType.ACTIVE -> response.body()!!.list.filter { it.status != MeetingStatus.CREATE.value }.sortedByDescending { getTimeStampFromString(it.date) }
                        MeetingFilterType.INCOMING -> response.body()!!.list.filter { it.status == MeetingStatus.CREATE.value }.sortedByDescending { getTimeStampFromString(it.date) }
                        MeetingFilterType.ALL -> response.body()!!.list.filter { it.status == MeetingStatus.COMPLETE.value
                                || it.status == MeetingStatus.CANCEL.value
                                || it.status == MeetingStatus.NOT_HAPPEN.value}.sortedByDescending { getTimeStampFromString(it.date) }
                        else -> response.body()!!.list.sortedByDescending { getTimeStampFromString(it.date) }
                    }
                    stateLiveData.postValue(State.LoveIsMeetingsLoadedState(loveIsList, type))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }


        }

    }

    fun setCurrentUser(user: User){
        currentUser = user
    }

    fun getCurrentUser() = currentUser

    fun getHistoryLoveIsMeetings(page: Int = 1, size: Int = 25) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getCurrentUserLoveIsMeetings(page, size)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.LoveIsMeetingsLoadedState(response.body()!!.list.filter { loveIs ->
                        loveIs.status == MeetingStatus.COMPLETE.value
                                || loveIs.status == MeetingStatus.CANCEL.value
                                || loveIs.status == MeetingStatus.NOT_HAPPEN.value

                    }, MeetingFilterType.HISTORY))
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun getEventIsMeetings(page: Int = 1, size: Int = 25, type: MeetingFilterType) {
        Log.d("debug", "getEvents")
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getEventIsMeetings(page, size, type)
            when (response?.code()) {
                200 -> {
                    val eventIsList = response.body()!!.list.sortedByDescending { getTimeStampFromString(it.date) }

                    stateLiveData.postValue(State.EventIsMeetingsLoadedState(eventIsList, type))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun getCurrentUserId() {
        stateLiveData.postValue(State.LoadedCurrentUserId(authRepository.getUserId()))
    }

    fun getCurrentUserInfo() {
        val user = mainRepository.getCurrentUser()
        user ?: return
        stateLiveData.postValue(State.LoadedCurrentUser(user))
    }

    fun completeLoveIs(id: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.completeLoveIs(id)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun completeEventIs(id: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.completeEventIs(id)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun changeLoveIsStatus(id: Long, status: MeetingStatus) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val currentUser = mainRepository.getCurrentUser()
            if(currentUser?.subscription == null){
                stateLiveData.postValue(State.SubsriptionNeededState)
                return@launch
            }
            val response = loveIsRepository.changeLoveIsStatus(id, status)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun acceptLoveIs(id: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val currentUser = mainRepository.getCurrentUser()
            if(currentUser?.subscription == null){
                stateLiveData.postValue(State.SubsriptionNeededState)
                return@launch
            }
            val response = loveIsRepository.acceptLoveIs(id)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun getEventIsMembers(page: Int = 1, size: Int = 25, eventIsId: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getEventMembers(page, size, eventIsId)
            when (response?.code()) {
                200 -> {
                    val members = response.body()!!.events //response.body()!!.events.map { member ->
                       // getUserById(member.user)
                    //}
                    //if (members.all { it != null })
                        stateLiveData.postValue(State.LoadedEventMembers(members))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun joinEventIs(eventId: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val currentUser = mainRepository.getCurrentUser()
            if(currentUser?.subscription == null){
                stateLiveData.postValue(State.SubsriptionNeededState)
                return@launch
            }
            val response = loveIsRepository.joinEventIs(eventId)
            when (response?.code()) {
                200 -> {
                    getEventIsMembers(eventIsId = eventId)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }

    }

    fun removeParticipantFromEventIs(eventId: Long, userId: Long){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.removeParticipantFromEventIs(eventId, userId)
            when (response?.code()) {
                204 -> {
                    getEventIsMembers(eventIsId = eventId)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun shareEventIs(id: Long, context: Context) {
        stateLiveData.postValue(State.LoadedIntent(loveIsRepository.getShareEventIntent(id, context)))
    }

    fun shareLoveIs(id: Long, context: Context) {
        stateLiveData.postValue(State.LoadedIntent(loveIsRepository.getShareLoveIsIntent(id, context)))
    }

    fun getEventIsById(id: Long) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getEventIsById(id)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.EventIsSingleMeetingLoadedState(response.body()!!))
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun getLoveIsById(id: Long) {
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

    private suspend fun getUserById(userId: Long): User? {
        val response = mainRepository.getUserById(userId)
        return when (response?.code()) {
            200 -> {
                response.body()
            }


            400 -> {
                stateLiveData.postValue(State.ErrorState(400))
                null
            }
            null -> {
                stateLiveData.postValue(State.ErrorState(0))
                null
            }
            else -> {
                stateLiveData.postValue(State.ErrorState(2))
                null
            }
        }

    }

    private fun getTimeStampFromString(date: String): Long{
        val dateStringList = date.split("-")
        val timeString = date.substringAfter("T").removeSuffix("+").split(":").subList(0, 2)
            .joinToString(":")
        val calendar = Calendar.getInstance().apply {
            set(
                dateStringList[0].toInt(),
                dateStringList[1].toInt() - 1,
                dateStringList[2].substringBefore("T").toInt(),
                timeString.split(":")[0].toInt(),
                timeString.split(":")[1].toInt()
            )
        }
        return calendar.timeInMillis
    }
}