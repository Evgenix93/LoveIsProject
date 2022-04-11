package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.PermissionRequest
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
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.databinding.FragmentRegistration5Binding
import com.project.loveis.viewmodels.RegistrationViewModel
import java.security.Permission
import java.security.Permissions
import java.util.jar.Manifest

class Registration5Fragment : Fragment(R.layout.fragment_registration5) {
    private val binding: FragmentRegistration5Binding by viewBinding()
    private val args: Registration5FragmentArgs by navArgs()
    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        bindViewModel()
        initPermissionLauncher()
        initEditText()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.LoadingState -> {showLoading(true)}
                is State.SuccessState -> {
                    showLoading(false)
                    (requireActivity() as MainActivity).showSuccessNotification()
                    findNavController().navigate(
                    Registration5FragmentDirections.actionRegistration5FragmentToPhoneNumber2Fragment(args.phone)
                )}

                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code){
                        400 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        409 -> {}
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
                is State.ErrorMessageState -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun initPermissionLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
            if(granted){
                viewModel.registerNewUser(
                    phone = args.phone,
                    gender = args.gender,
                    photo = args.photo,
                    name = args.name,
                    birthDate = args.birthDate,
                    about = binding.bioEditText.text.toString()
                )
            }
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

    private fun initEditText(){
       // binding.bioEditText.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
       // binding.bioEditText.setOnEditorActionListener {_, action, _ ->
       //     Log.d("mylog", "editor action")
        //    if(action == EditorInfo.IME_FLAG_NO_ENTER_ACTION) {
         //       (requireActivity() as MainActivity).hideKeyboard()
         //   }
         //   false

       // }
    }
}