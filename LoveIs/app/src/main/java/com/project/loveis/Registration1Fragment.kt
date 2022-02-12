package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.util.Gender


class Registration1Fragment : Fragment(R.layout.fragment_registration1) {
    private val binding: FragmentRegistration1Binding by viewBinding()
    private val args: Registration1FragmentArgs by navArgs()
    private var gender = Gender.male

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showEnabledGender(gender)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(
                Registration1FragmentDirections.actionRegistration1FragmentToRegistration2Fragment(
                    phone = args.phone,
                    gender = gender.name
                )
            )
        }

        binding.maleImage.setOnClickListener {
            gender = Gender.male
            showEnabledGender(gender)
        }

        binding.femaleImage.setOnClickListener {
            gender = Gender.female
            showEnabledGender(gender)
        }
    }


    private fun showEnabledGender(gender: Gender) {
        binding.maleImage.drawable.setTint(
            resources.getColor(
                if (gender == Gender.male) {
                    R.color.blue
                } else {
                    R.color.gray
                }
            )
        )

        binding.femaleImage.drawable.setTint(
            resources.getColor(
                if (gender == Gender.male) {
                    R.color.gray
                } else {
                    R.color.pink
                }
            )
        )

        binding.maleTextView.setTextColor(
            resources.getColor(
                if (gender == Gender.male) {
                    R.color.blue
                } else {
                    R.color.gray
                }
            )
        )

        binding.femaleTextView.setTextColor(
            resources.getColor(
                if (gender == Gender.male) {
                    R.color.gray
                } else {
                    R.color.pink
                }
            )
        )


    }
}