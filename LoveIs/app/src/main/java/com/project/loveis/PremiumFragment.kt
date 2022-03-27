package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentPremiumBinding
import com.project.loveis.models.User
import com.project.loveis.viewmodels.SubsriptionViewModel
import java.text.SimpleDateFormat
import java.util.*

class PremiumFragment: Fragment(R.layout.fragment_premium) {
    private val binding: FragmentPremiumBinding by viewBinding()
    private val viewModel: SubsriptionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setClickListeners()
        bindViewModel()
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.premium)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setClickListeners(){
        binding.continueButton.setOnClickListener {
            viewModel.confirmSubsription(0, convertCurrentDateToString() )
        }
    }

    private fun convertCurrentDateToString(): String{
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
            .apply { timeZone = TimeZone.getDefault() }

        val calendar = Calendar.getInstance().apply { timeInMillis = System.currentTimeMillis() }
        val date = Date(
            calendar.get(Calendar.YEAR) - 1900,
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DATE),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)

        )

        return dateFormat.format(date)



    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner, androidx.lifecycle.Observer { state ->
            when(state){
                is State.SuccessState -> {
                    findNavController().navigate(R.id.profileFragment)
                    }
                is State.LoadingState -> {}
                is State.ErrorState -> {
                    when(state.code){
                        400 -> {showToast("ошибка")}
                        0 -> {findNavController().navigate(R.id.errorFragment)}
                        1 -> {findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToPhoneNumber1Fragment())}
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
            }


        })
    }
}