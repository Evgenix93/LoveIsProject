package com.project.loveis

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentEditBinding
import com.project.loveis.models.User
import com.project.loveis.viewmodels.ProfileViewModel

class EditProfileFragment : Fragment(R.layout.fragment_edit) {
    private val binding: FragmentEditBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavBar()
        setClickListeners()
        bindViewModel()
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.edit)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.StartState -> {
                    viewModel.getUserInfo()
                }
                is State.LoadingState -> showLoading(true)
                is State.LoadedSingleState -> {
                    showLoading(false)
                    showUserInfo(state.result as User)
                }
                is State.SuccessState -> {
                    showLoading(false)
                    findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment())
                }

                is State.ErrorState -> {
                    showLoading(false)
                    when(state.code){
                        400 -> (requireActivity() as MainActivity).showErrorNotification()
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }

            }


        })
    }

    private fun setClickListeners(){
        binding.continueBtn.setOnClickListener {
            viewModel.updateUserInfo(binding.nameEditText.text.toString(), binding.editText.text.toString())
        }

    }

    private fun hideBottomNavBar(){
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
    }

    private fun showLoading(loading: Boolean){
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

    private fun showUserInfo(user: User) {
        binding.nameEditText.text.append(user.name)
        binding.editText.text.append(user.about)

    }


}