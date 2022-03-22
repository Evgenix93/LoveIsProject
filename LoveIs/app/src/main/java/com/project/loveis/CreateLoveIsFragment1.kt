package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.card.MaterialCardView
import com.project.loveis.databinding.FragmentCreateLoveis1Binding

class CreateLoveIsFragment1: Fragment(R.layout.fragment_create_loveis_1) {
    private val binding: FragmentCreateLoveis1Binding by viewBinding()
    private var type = 1
   // private val args = arguments?.getInt("user id")!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavBar()
        initTypeCardViews()
        initContinueButton()
    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun hideBottomNavBar(){
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
    }

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(CreateLoveIsFragment1Directions.actionCreateLoveIsFragment1ToCreateLoveIsFragment2(type, arguments?.getLong("user id")!!))
        }
    }

    private fun initTypeCardViews(){
        binding.walkCardView.setOnClickListener{
            checkTypeCardView(binding.walkCardView, binding.walkIcon, binding.type1Description)
            uncheckTypeCardView(binding.winterSportsCardView, binding.skiIcon, binding.type2Description)
            uncheckTypeCardView(binding.tripCardView, binding.carIcon, binding.type3Description)
            uncheckTypeCardView(binding.sushiCardView, binding.rollsIcon, binding.type4Description)
            uncheckTypeCardView(binding.cafeCardView, binding.cafeIcon, binding.type5Description)
            type = 1
        }
        binding.winterSportsCardView.setOnClickListener{
            uncheckTypeCardView(binding.walkCardView, binding.walkIcon, binding.type1Description)
            checkTypeCardView(binding.winterSportsCardView, binding.skiIcon, binding.type2Description)
            uncheckTypeCardView(binding.tripCardView, binding.carIcon, binding.type3Description)
            uncheckTypeCardView(binding.sushiCardView, binding.rollsIcon, binding.type4Description)
            uncheckTypeCardView(binding.cafeCardView, binding.cafeIcon, binding.type5Description)
            type = 2
        }

        binding.tripCardView.setOnClickListener{
            uncheckTypeCardView(binding.walkCardView, binding.walkIcon, binding.type1Description)
            uncheckTypeCardView(binding.winterSportsCardView, binding.skiIcon, binding.type2Description)
            checkTypeCardView(binding.tripCardView, binding.carIcon, binding.type3Description)
            uncheckTypeCardView(binding.sushiCardView, binding.rollsIcon, binding.type4Description)
            uncheckTypeCardView(binding.cafeCardView, binding.cafeIcon, binding.type5Description)
            type = 3
        }

        binding.sushiCardView.setOnClickListener{
            uncheckTypeCardView(binding.walkCardView, binding.walkIcon, binding.type1Description)
            uncheckTypeCardView(binding.winterSportsCardView, binding.skiIcon, binding.type2Description)
            uncheckTypeCardView(binding.tripCardView, binding.carIcon, binding.type3Description)
            checkTypeCardView(binding.sushiCardView, binding.rollsIcon, binding.type4Description)
            uncheckTypeCardView(binding.cafeCardView, binding.cafeIcon, binding.type5Description)
            type = 4
        }

        binding.cafeCardView.setOnClickListener{
            uncheckTypeCardView(binding.walkCardView, binding.walkIcon, binding.type1Description)
            uncheckTypeCardView(binding.winterSportsCardView, binding.skiIcon, binding.type2Description)
            uncheckTypeCardView(binding.tripCardView, binding.carIcon, binding.type3Description)
            uncheckTypeCardView(binding.sushiCardView, binding.rollsIcon, binding.type4Description)
            checkTypeCardView(binding.cafeCardView, binding.cafeIcon, binding.type5Description)
            type = 5
        }
    }

    private fun checkTypeCardView(typeCardView: MaterialCardView, iconImageView: ImageView, descriptionTextView: TextView){
        with(typeCardView){
            cardElevation = 20f
            strokeWidth = 0
        }
        iconImageView.drawable.setTint(getColor(requireContext(), R.color.blue))
        descriptionTextView.setTextColor(getColor(requireContext(), R.color.blue))
    }

    private fun uncheckTypeCardView(typeCardView: MaterialCardView, iconImageView: ImageView, descriptionTextView: TextView){
        with(typeCardView){
            cardElevation = 0f
            strokeWidth = 3
        }
        iconImageView.drawable.setTint(getColor(requireContext(), R.color.gray))
        descriptionTextView.setTextColor(getColor(requireContext(), R.color.gray))
    }

}