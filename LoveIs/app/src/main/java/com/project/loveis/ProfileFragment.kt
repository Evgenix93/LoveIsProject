package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentProfileBinding
import com.project.loveis.models.User
import com.project.loveis.viewmodels.ProfileViewModel
import java.util.*

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        showBottomNavigation()
        setClickListeners()
        initFilePickerLauncher()
        bindViewModel()
    }





    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().resources.getString(R.string.profile)
            burgerMenu.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMenuFragment())

            }

        }
    }

    private fun showBottomNavigation(){
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
    }

    private fun setClickListeners(){
        binding.verifyBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToVerify1Fragment())
        }
        binding.editFloatingBtn.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }

        binding.uploadPhotoImageView.setOnClickListener {
            filePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun showProfileInfo(user: User){
        val photoUrl = "https://loveis.scratch.studio/" + user.photo
        Glide.with(this)
            .load(photoUrl)
            .into(binding.mainPhotoImageView)

        val strings = user.birthday.split('-')


        val birthDate = Calendar.getInstance().apply {
            set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
        }

        val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        binding.nameTextView.text = "${user.name}, $age"

    }

    private fun initFilePickerLauncher(){
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
            viewModel.updateUserPhoto(uri)
        }

    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.StartState -> {
                    viewModel.performAuth()
                }
                is State.SuccessState -> {
                    viewModel.getUserInfo()}
                is State.LoadingState -> {showLoading(true)}
                is State.LoadedSingleState -> {
                    showLoading(false)
                    val user = state.result as User
                    showProfileInfo(user)
                }
                is State.ErrorState -> {
                    showLoading(false)
                    when(state.code){
                        404 -> {}
                        0 -> {findNavController().navigate(R.id.errorFragment)}
                        1 -> {findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPhoneNumber1Fragment())}
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
            }
        })
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }
}