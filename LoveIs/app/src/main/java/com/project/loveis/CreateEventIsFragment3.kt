package com.project.loveis

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateEventis3Binding

class CreateEventIsFragment3: Fragment(R.layout.fragment_create_eventis_3) {
    private val binding: FragmentCreateEventis3Binding by viewBinding()
    private val args: CreateEventIsFragment3Args by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSwitches()
        initEventCostTextView()
        initContinueButton()
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.create_event_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initSwitches() {
        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            with(binding) {
                switchCompat.apply{
                    thumbTintList = if(!isChecked)
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray6)
                    else null
                    trackTintList = if(!isChecked)
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray4)
                    else null
                }
                switchCompat2.isChecked = !isChecked
                freeEventTextView.setTextColor(ResourcesCompat.getColor(resources, if(isChecked) R.color.blue else R.color.gray6, null))
            }
        }

        binding.switchCompat2.setOnCheckedChangeListener{_, isChecked ->
            with(binding) {
                switchCompat2.apply{
                    thumbTintList = if(!isChecked)
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray6)
                    else null
                    trackTintList = if(!isChecked)
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray4)
                    else null
                }
                switchCompat.isChecked = !isChecked
                paidEventTextView.setTextColor(ResourcesCompat.getColor(resources, if(isChecked) R.color.blue else R.color.gray6, null))
                enableCostEventTextView(isChecked)
            }
        }
    }

    private fun enableCostEventTextView(isChecked: Boolean) {
        with(binding) {
            plusIcon.apply{
                isEnabled = isChecked
                imageTintList =  AppCompatResources.getColorStateList(requireContext(), if (isChecked) R.color.blue else R.color.gray6)
            }

            eventCostTextView.setTextColor(
                ResourcesCompat.getColor(
                    resources,
                    if (isChecked) R.color.blue
                    else R.color.gray6,
                    null))

            minusIcon.apply{
                isEnabled = isChecked
                imageTintList =  AppCompatResources.getColorStateList(requireContext(), if (isChecked) R.color.blue else R.color.gray6)
            }
        }
    }

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
            val price = if(binding.switchCompat.isChecked)
                0
            else
               binding.eventCostTextView.text.toString().toInt()
            findNavController().navigate(CreateEventIsFragment3Directions.actionCreateEventIsFragment3ToCreateEventIsFragment4(args.type, args.personCount, price))
        }
    }

   private fun initEventCostTextView(){
        binding.plusIcon.setOnClickListener {
            var price = binding.eventCostTextView.text.toString().toInt()
            price++
            binding.eventCostTextView.setText(price.toString())
        }
        binding.minusIcon.setOnClickListener {
            var price = binding.eventCostTextView.text.toString().toInt()
            price--
            binding.eventCostTextView.setText(price.toString())
        }
    }
}