package com.project.loveis.adapters

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.loveis.UserFragment
import com.project.loveis.models.User
import okhttp3.internal.notify
import okhttp3.internal.notifyAll

class UsersViewPagerAdapter(fragment: Fragment, private val onClick: () -> Unit): FragmentStateAdapter(fragment) {
    private var users = listOf<User>()
    private lateinit var currentUser: User

    override fun getItemCount(): Int {
        Log.d("MyDebug", "getItemCount size = ${users.size}")
        return users.size
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("MyDebug", "createFragment position = $position")
       return UserFragment(){onClick()}.apply { arguments = Bundle().apply {
           putParcelable(USER, currentUser)
           putParcelable(USERS, users[position])
       } }
    }

    fun updateList(newList: List<User>){
        Log.d("MyDebug", "updateList")
        users = newList
        notifyDataSetChanged()
        notifyDataSetChanged()
    }


    fun setCurrentUser(user: User){
        currentUser = user
    }

    companion object {
        const val USER = "user"
        const val USERS = "users"
    }
}