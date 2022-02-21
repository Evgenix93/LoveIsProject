package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentProfileBinding
import com.project.loveis.models.User
import com.project.loveis.singletones.Tokens
import com.project.loveis.viewmodels.ProfileViewModel
import java.util.*

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private val binding: FragmentProfileBinding by viewBinding()
    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var filePickerLauncher2: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        showBottomNavigation()
        setClickListeners()
        initFilePickerLaunchers()
        bindViewModel()
    }


    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().resources.getString(R.string.profile)
            burgerMenu.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToMenuFragment())

            }
            logOut.setOnClickListener {
                Tokens.token = ""
                findNavController().navigate(R.id.phoneNumber1Fragment)
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

        binding.addPhoto1.setOnClickListener {
            viewModel.deleteAdditionalPhoto(1)
        }
        binding.addPhoto2.setOnClickListener {
            viewModel.deleteAdditionalPhoto(2)
        }

        binding.addPhoto3.setOnClickListener {
            viewModel.deleteAdditionalPhoto(3)
        }
    }

    private fun showProfileInfo(user: User){
        val prefix = "https://loveis.scratch.studio/"
        val mainPhoto = prefix + user.photo
        Glide.with(this)
            .load(mainPhoto)
            .into(binding.mainPhotoImageView)
         Log.d("Debug", user.images.toString())
        user.images.forEach{image ->
            when(image.number){
                1 -> Glide.with(this)
                    .load(prefix + image.url)
                    .into(binding.additionalPhoto1ImageView)
                2 -> Glide.with(this)
                    .load(prefix + image.url)
                    .into(binding.additionalPhoto2ImageView)
                3 -> Glide.with(this)
                    .load(prefix + image.url)
                    .into(binding.additionalPhoto3ImageView)
            }
        }

        val strings = user.birthday.split('-')

        val birthDate = Calendar.getInstance().apply {
            set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
        }

        val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)

        binding.nameTextView.text = "${user.name}, $age"

    }

    private fun initFilePickerLaunchers(){
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
            viewModel.updateUserPhoto(uri)
        }
        filePickerLauncher2 = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
            viewModel.updateAdditionalPhoto(uri)
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
                    if(state.result is User) {
                        val user = state.result as User
                        showProfileInfo(user)
                    }else
                        filePickerLauncher2.launch(arrayOf("image/*"))
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