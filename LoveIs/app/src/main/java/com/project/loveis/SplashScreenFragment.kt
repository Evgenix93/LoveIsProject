package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.project.loveis.viewmodels.SplashScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenFragment: Fragment(R.layout.fragment_logo) {
    private val viewModel: SplashScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        observeState()
        performAuth()
        lifecycleScope.launch {
            delay(1500)
        }

    }

    private fun hideBottomNavigation() {
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    private fun performAuth(){
        viewModel.performAuth()
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.SuccessState -> findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToProfileFragment())
                is State.ErrorState -> {
                    Log.e("MyDebug", "error code = ${state.code}")
                    when(state.code){
                        404 -> {}
                        0 -> {findNavController().navigate(R.id.errorFragment)}
                        1 -> {findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToPhoneNumber1Fragment())}
                        2 -> {
                            Log.e("MyDebug", "error code 2")
                            findNavController().navigate(R.id.serverErrorFragment)
                        }
                    }
                }
                is State.ErrorMessageState -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                is State.LoadingState -> {}
                else -> {
                    Log.e("MyDebug", "server Error Fragment")
                    findNavController().navigate(R.id.serverErrorFragment)
                }
            }
        }
    }

}