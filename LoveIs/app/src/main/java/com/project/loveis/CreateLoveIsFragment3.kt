package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.databinding.FragmentCreateLoveisEventis5Binding
import com.project.loveis.dialogs.MenuBottomDialogDateFragment
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
        initSteps()
        initContinueButton()
        initEditText()
    }

    private fun initSteps(){
        binding.stepTextView2.text = "3"
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
            if(binding.dateTextView.text.isBlank() || binding.timeTextView.text.isBlank() ){
                Toast.makeText(requireContext(), "Введите дату", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dateString = binding.dateTextView.text.toString()
            val year = dateString.substringAfterLast('.').toInt()
            val month = dateString.substringAfter('.').substringBefore('.').toInt()
            val day = dateString.substringBefore('.').toInt()
            val timeString = binding.timeTextView.text.toString()
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
        binding.dateTextView.setOnClickListener{
            MenuBottomDialogDateFragment(false, {day, month, year ->
                val currentCalendar = Calendar.getInstance()
                val choiceCalendar = Calendar.getInstance().apply { set(year, month - 1, day) }
                val timeDiff = choiceCalendar.timeInMillis - currentCalendar.timeInMillis
                val currDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
                val currMonth = currentCalendar.get(Calendar.MONTH) + 1
                val currYear = currentCalendar.get(Calendar.YEAR)
               val dayStr = if(day < 10) "0$day" else day.toString()
               val monthStr = if(month < 10) "0$month" else month.toString()
               val yearStr = year.toString().takeLast(2)
                if(timeDiff > 0)
               binding.dateTextView.text = "$dayStr.$monthStr.$yearStr"
                else{
                    val currDayStr = if(currDay < 10) "0$currDay" else currDay.toString()
                    val currMonthStr = if(currMonth < 10) "0$currMonth" else currMonth.toString()
                    val currYearStr = currYear.toString().takeLast(2)
                    binding.dateTextView.text = "$currDayStr.$currMonthStr.$currYearStr"
                }
            }, {_,_-> }).show(childFragmentManager, null)
        }

        binding.timeTextView.setOnClickListener{
            MenuBottomDialogDateFragment(true, {_,_,_->}, {hour, minute ->
                val chosenDate = binding.dateTextView.text.split(".")
                val currentCalendar = Calendar.getInstance()
                val choiceCalendar = if(binding.dateTextView.text.isNotEmpty()) Calendar.getInstance()
                    .apply { set(chosenDate[2].prependIndent("20").toInt(), chosenDate[1].toInt() - 1, chosenDate[0].toInt(),
                        hour, minute)}
                else null
                if(choiceCalendar != null) {
                    val timeDiff = choiceCalendar.timeInMillis - currentCalendar.timeInMillis
                    if(timeDiff > 0)
                        binding.timeTextView.text = "$hour:$minute"
                    else
                        binding.timeTextView.text =
                            "${currentCalendar.get(Calendar.HOUR_OF_DAY) + 1}:${currentCalendar.get(Calendar.MINUTE)}"

                }else
                     binding.timeTextView.text = "$hour:$minute"
            }).show(childFragmentManager, null)
        }
    }
}