package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.databinding.FragmentCreateLoveisEventis6Binding

class CreateLoveIsFragment4 : Fragment(R.layout.fragment_create_loveis_eventis_6) {
    private val binding: FragmentCreateLoveisEventis6Binding by viewBinding()
    private val args: CreateLoveIsFragment4Args by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSteps()
        initContinueButton()
    }


    private fun initSteps(){
        binding.stepTextView2.text = "4"
        binding.stepTextView4.text = "4"
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.create_love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initContinueButton() {
        binding.continueBtn.setOnClickListener {
            val telegramUrl = binding.telegramRefEditText.text.toString()
            val whatsappUrl = binding.whatsAppRefEditText.text.toString()
            findNavController().navigate(CreateLoveIsFragment4Directions.actionCreateLoveIsFragment4ToCreateLoveIsManFragment5(
              args.type,
              args.place,
              args.date,
              telegramUrl,
              whatsappUrl,
                args.userId,
            ))
        }
    }

}