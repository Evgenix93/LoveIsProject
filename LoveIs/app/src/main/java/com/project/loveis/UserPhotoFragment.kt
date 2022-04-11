package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentUserPhotoBinding

class UserPhotoFragment: Fragment(R.layout.fragment_user_photo) {
    private val binding: FragmentUserPhotoBinding by viewBinding()
    private var onClick: () -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(arguments?.getString(PHOTO_KEY).orEmpty())
    }


    fun bind(photo: String){
        Glide.with(this)
            .load(photo)
            .into(binding.photoImageView)
        binding.root.setOnClickListener {
            Log.d("MyDebug", "onClick viewPager")
            //onClick()
        }
    }

    companion object{
        const val PHOTO_KEY = "photo_key"

        fun newInstance(onclick: () -> Unit): UserPhotoFragment{
            return UserPhotoFragment().apply { this.onClick = onclick }
        }
    }
}