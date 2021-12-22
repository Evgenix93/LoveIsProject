package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentVideoBinding

class VideoFragment: Fragment(R.layout.fragment_video) {
    private val binding: FragmentVideoBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initVideoList()
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.video)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initVideoList(){
        with(binding.videoRecyclerView){
            adapter = VideoAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}