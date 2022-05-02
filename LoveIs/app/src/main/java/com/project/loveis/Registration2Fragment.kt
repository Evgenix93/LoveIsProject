package com.project.loveis

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.databinding.FragmentRegistration2Binding
import com.project.loveis.viewmodels.RegistrationViewModel

class Registration2Fragment : Fragment(R.layout.fragment_registration2) {
    private val binding: FragmentRegistration2Binding by viewBinding()
    private val args: Registration2FragmentArgs by navArgs()
    private var photoUri: Uri? = null
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilePickerLauncher()
        setOnClickListener()
        bindViewModel()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(
                Registration2FragmentDirections.actionRegistration2FragmentToRegistration3Fragment(
                    phone = args.phone,
                    gender = args.gender,
                    photo = if (photoUri != null) photoUri.toString() else null
                )
            )
        }

        binding.uploadPhotoImageView.setOnClickListener {
            filePickerLauncher.launch(arrayOf("image/*"))
        }

    }

    private fun initFilePickerLauncher() {
        filePickerLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                viewModel.checkFileSize(uri)
                photoUri = uri


            }
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.SuccessState -> {
                    binding.loader.isVisible = false
                    Glide.with(this)
                        .load(photoUri)
                        .into(binding.photoImageView)
                }

                is State.ErrorMessageState -> {
                    binding.loader.isVisible = false
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                is State.LoadingState -> binding.loader.isVisible = true
            }




        })
    }
}