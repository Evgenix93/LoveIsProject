package com.project.loveis

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.allViews
import androidx.core.view.descendants
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.project.loveis.databinding.FragmentLoveIsBinding

class LoveIsFragment: Fragment(R.layout.fragment_love_is) {
    private val binding: FragmentLoveIsBinding by viewBinding()
    private val loveIsAdapter = LoveIsEventIsAdapter{
        findNavController().navigate(LoveIsFragmentDirections.actionLoveIsFragmentToLoveIsDetailsFragment())
    }
    private val finishedLoveIsAdapter = FinishedLoveIsAdapter()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initChips()
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)

    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.love_is)
            logOut.isVisible = false
            burgerMenu.isVisible = false
        }

    }

    private fun initList(){
        with(binding.list){
            adapter = loveIsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun initChips(){
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("debug", "checked")
            group.descendants.forEach {
                val chip = it as Chip
                if(chip.isChecked){
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.elevation = 3f

                }else{
                    chip.setChipBackgroundColorResource(R.color.chips_background)
                    chip.elevation = 0f
                }
            }

            when(checkedId){
                R.id.activeChip ->  {binding.list.adapter = loveIsAdapter}
                R.id.myChip -> {}
                R.id.newChip -> {}
                R.id.historyChip -> {binding.list.adapter = finishedLoveIsAdapter}

            }
        }
    }




}