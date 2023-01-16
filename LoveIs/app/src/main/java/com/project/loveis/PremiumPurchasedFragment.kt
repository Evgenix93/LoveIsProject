package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding

import com.project.loveis.databinding.FragmentPremiumPurchasedBinding
import com.project.loveis.singletones.ProfileInfo

class PremiumPurchasedFragment: Fragment(R.layout.fragment_premium_purchased) {
    private val binding: FragmentPremiumPurchasedBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initDateTextView()
    }

    private fun initToolbar(){
        binding.toolbar.logOut.setImageResource(R.drawable.ic_arrow_back)
        binding.toolbar.burgerMenu.isVisible = false
        binding.toolbar.title.text = getString(R.string.premium)
        binding.toolbar.logOut.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initDateTextView(){
        val dateStringList = ProfileInfo.currentUser!!.subscription!!.until.split("-")
        val date = dateStringList[2].substringBefore('T')
        val month = dateStringList[1]
        val year = dateStringList[0]
        binding.statusTextView.text = "Премиум до $date.$month.$year"
    }
}