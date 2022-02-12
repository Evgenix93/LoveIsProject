package com.project.loveis.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.loveis.UserFragment
import com.project.loveis.models.User

class UsersViewPagerAdapter(fragment: Fragment, private val onClick: () -> Unit): FragmentStateAdapter(fragment) {
    private var users = listOf<User>()
    private lateinit var currentUser: User

    override fun getItemCount(): Int {
        return users.size
    }

    override fun createFragment(position: Int): Fragment {
       return UserFragment(){onClick()}.apply { arguments = Bundle().apply {
           putParcelable(USER, currentUser)
           putParcelable(USERS, users[position])
       } }
    }

    fun updateList(newList: List<User>){
        users = newList
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