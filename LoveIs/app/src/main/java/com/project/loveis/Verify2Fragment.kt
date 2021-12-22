package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentVerify2Binding
import com.project.loveis.viewmodels.VerifyViewModel
import java.io.File

class Verify2Fragment : Fragment(R.layout.fragment_verify2) {
    private val binding: FragmentVerify2Binding by viewBinding()
    private val viewModel: VerifyViewModel by viewModels()
    private lateinit var openDocumentLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initOpenDocumentLauncher()
        setOnClickListeners()
        bindViewModel()
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(com.project.loveis.R.string.verification)
            logOut.setImageResource(com.project.loveis.R.drawable.ic_arrow_back)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setOnClickListeners() {
        binding.continueBtn.setOnClickListener {
            viewModel.verify()
        }

        binding.selfieCardView.setOnClickListener {
            openDocumentLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.LoadingState -> showLoading(true)
                is State.SuccessState -> {
                    showLoading(false)
                    (requireActivity() as MainActivity).showSuccessNotification()
                    findNavController().navigate(Verify2FragmentDirections.actionVerify2FragmentToProfileFragment())
                }
                is State.LoadedSingleState -> {
                }
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code){
                        400 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        410 -> toast("Пользователь уже верифицирован")
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
            }
        }
    }

    private fun initOpenDocumentLauncher() {
        openDocumentLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) {uri ->
            Glide.with(binding.root)
                .load(uri)
                .into(binding.selfiePhotoImageView)
            viewModel.safePhotoUri(uri)
        }
    }


    private fun toast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(loading: Boolean) {
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }
}
