package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.databinding.FragmentCreateLoveisEventis5Binding
import java.lang.reflect.Array.set
import java.text.DateFormat
import java.text.DateFormat.DEFAULT
import java.text.SimpleDateFormat
import java.util.*

class CreateLoveIsFragment3 : Fragment(R.layout.fragment_create_loveis_eventis_5) {
    private val binding: FragmentCreateLoveisEventis5Binding by viewBinding()
    private val args: CreateLoveIsFragment3Args by navArgs()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
        .apply { timeZone = TimeZone.getDefault() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()
        initEditText()
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
            val dateString = binding.dateEditText.text.toString()
            val year = dateString.substringAfterLast('.').toInt()
            val month = dateString.substringAfter('.').substringBefore('.').toInt()
            val day = dateString.substringBefore('.').toInt()
            val timeString = binding.timeEditText.text.toString()
            val hours = timeString.substringBefore(':').toInt()
            val minutes = timeString.substringAfter(':').toInt()
            val date = Date(
                2000 + year - 1900,
                month - 1,
                day,
                hours,
                minutes
            )
            val formattedDateString = dateFormat.format(date)
            Log.d("Debug", "formatted string = $formattedDateString")
            findNavController().navigate(
                CreateLoveIsFragment3Directions.actionCreateLoveIsFragment3ToCreateLoveIsFragment4(
                    args.type,
                    args.place,
                    formattedDateString,
                    args.userId
                )
            )
        }
    }

    private fun initEditText(){
        binding.dateEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_DONE){
                (requireActivity() as MainActivity).hideKeyboard()
                true
            }
            else
                false
        }

        binding.timeEditText.setOnEditorActionListener{_, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE){
                (requireActivity() as MainActivity).hideKeyboard()
                true
            }
            else
                false
        }
    }

}