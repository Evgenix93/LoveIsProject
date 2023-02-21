package com.project.loveis

import android.content.Intent
import com.project.loveis.models.EventIs
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.User

sealed class State {
    object LoadingState: State()
    object StartState: State()
    object SuccessState: State()
    class ErrorState(val code: Int): State()
    class ErrorMessageState(val message: String): State()
    class LoadedSingleState(val result: Any): State()
    class ReadMessageState(val read: Boolean): State()
    class LoveIsSingleMeetingLoadedState(val meeting: LoveIs): State()
    class LoveIsMeetingsLoadedState(val meetings: List<LoveIs>, val type: MeetingFilterType): State()
    class EventIsSingleMeetingLoadedState(val meeting: EventIs): State()
    class EventIsMeetingsLoadedState(val meetings: List<EventIs>, val type: MeetingFilterType): State()
    class LoadedCurrentUserId(val id: Long): State()
    class LoadedCurrentUser(val user: User): State()
    class LoadedEventMembers(val members: List<User>): State()
    class LoadedIntent(val intent: Intent): State()
    object SubsriptionNeededState: State()
    class SharedUserLoaded(val user: User): State()

}