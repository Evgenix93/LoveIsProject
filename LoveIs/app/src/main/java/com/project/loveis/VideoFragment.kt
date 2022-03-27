package com.project.loveis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.VideoAdapter
import com.project.loveis.databinding.FragmentVideoBinding
import com.project.loveis.models.Video
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.VideoViewModel

class VideoFragment: Fragment(R.layout.fragment_video) {
    private val binding: FragmentVideoBinding by viewBinding()
    private val viewModel: VideoViewModel by viewModels()
    private var videoAdapter: VideoAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initVideoList()
        observeState()
        getVideos()
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
        videoAdapter = VideoAdapter{
            binding.loadingProgressBar.isVisible = true
            viewModel.getVideoIntent(it)
        }
        with(binding.videoRecyclerView){
            adapter = videoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadedSingleState -> {
                    if(state.result is List<*>) {
                        binding.loadingProgressBar.isVisible = false
                        videoAdapter.updateList(state.result as List<Video>)
                    }
                    else{
                        binding.loadingProgressBar.isVisible = false
                        try {
                           startActivity(state.result as Intent)
                        }catch (e: Throwable){
                           Log.e("MyDebug", "startActivity error =  ${e.message}")
                            Toast.makeText(requireContext(), "Ошибка ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is State.ErrorState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Ошибка ${state.code}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun getVideos(){
       binding.loadingProgressBar.isVisible = true
       viewModel.getVideos()
    }
}