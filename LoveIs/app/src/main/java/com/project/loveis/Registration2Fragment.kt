package com.project.loveis

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.databinding.FragmentRegistration2Binding

class Registration2Fragment : Fragment(R.layout.fragment_registration2) {
    private val binding: FragmentRegistration2Binding by viewBinding()
    private val args: Registration2FragmentArgs by navArgs()
    private var photoUri: Uri? = null
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilePickerLauncher()
        setOnClickListener()
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
                Glide.with(this)
                    .load(uri)
                    .into(binding.photoImageView)

                photoUri = uri


            }
    }
}