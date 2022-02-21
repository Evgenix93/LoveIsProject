package com.project.loveis.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.loveis.UserPhotoFragment

class UserPhotoViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    var photoList = listOf<String>()
    override fun getItemCount(): Int {
        return photoList.size

    }

    override fun createFragment(position: Int): Fragment {
        return UserPhotoFragment().apply {
            arguments = Bundle().apply {
                putString(UserPhotoFragment.PHOTO_KEY, photoList[position])
            }
        }

    }

    fun updateList(newList: List<String>){
        photoList = newList
        notifyDataSetChanged()
    }
}