package com.project.loveis

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateEventis2Binding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding

class CreateEventIsFragment2: Fragment(R.layout.fragment_create_eventis_2) {
    private val binding: FragmentCreateEventis2Binding by viewBinding()
    private val args: CreateEventIsFragment2Args by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initPersonCounter()
        initContinueButton()
    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_event_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initPersonCounter(){
        var personCount: Int
        binding.plusIcon.setOnClickListener {
            personCount = binding.personCountTextView.text.toString().toIntOrNull() ?: 0
            //if(personCount < 10){
                personCount++
                binding.personCountTextView.setText(personCount.toString())
           // }
        }

        binding.minusIcon.setOnClickListener {
            personCount = binding.personCountTextView.text.toString().toInt()
            if(personCount > 3){
                personCount--
                binding.personCountTextView.setText(personCount.toString())

            }
        }

        binding.personCountTextView.addTextChangedListener(object : TextWatcher {
            var countStr = binding.personCountTextView.text.toString()
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("mylog", text.toString())
                val count = text.toString().toIntOrNull()
                // countStr = text.toString()
                //binding.personCountTextView.setText("")
                //if(text != null && text.length > 1){
                   // if(text[0] != 1.toChar()){
                  //      binding.personCountTextView.setText(text[1].toString())
                  //  }
              //  }

               // if(count == null){
                //binding.personCountTextView.setText(countStr)
               // }else{
                //   countStr = text.toString()
               // }



            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val count = text.toString().toIntOrNull()

                //if(text != null && text.length > 1){
                // if(text[0] != 1.toChar()){
                 //    binding.personCountTextView.setText(text[0].toString())
                // }
                 // }



               // if(count != null && count > 10){
                   // binding.personCountTextView.setText(10.toString())
                //}

                if(count != null && count < 3){
                   // Log.d("mylog", count.toString())
                    binding.personCountTextView.setText(3.toString())
                    // countStr = text.toString()
                }


                //if(count == null){
                 //   binding.personCountTextView.setText(countStr)
                //}else{
                //    countStr = text.toString()
                //}
                //binding.personCountTextView.setText(text)


            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.personCountTextView.setOnFocusChangeListener { _, focused ->
            //if(focused){
           //     binding.personCountTextView.setText("")
           // }

        }
    }

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
            val count = binding.personCountTextView.text.toString().toIntOrNull()
            count ?: run {
                Toast.makeText(requireContext(), "укажите количество мест", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            findNavController().navigate(CreateEventIsFragment2Directions.actionCreateEventIsFragment2ToCreateEventIsFragment3(args.type, count))
        }
    }
}