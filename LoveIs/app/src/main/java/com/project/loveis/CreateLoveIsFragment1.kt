package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.card.MaterialCardView
import com.project.loveis.adapters.TypesAdapter
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.models.Type
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.CreateLoveIsEventIsViewModel

class CreateLoveIsFragment1: Fragment(R.layout.fragment_create_loveis_1) {
    private val binding: FragmentCreateLoveis1Binding by viewBinding()
    private var type = 1L
    private val args: CreateLoveIsFragment1Args by navArgs()
    private val viewModel: CreateLoveIsEventIsViewModel by viewModels()
    private var typesAdapter: TypesAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavBar()
        initContinueButton()
        initTypesRecyclerView()
        observeState()
        getTypes()
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
            findNavController().navigate(CreateLoveIsFragment1Directions.actionCreateLoveIsFragment1ToCreateLoveIsFragment2(type.toInt(), args.userId))
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

    private fun getTypes(){
       viewModel.getTypes()
    }

    private fun initTypesRecyclerView(){
        binding.typesRecyclerView.adapter = TypesAdapter{type = it}.also{typesAdapter = it}
        binding.typesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadedSingleState -> typesAdapter.updateList(state.result as List<Type>)
                is State.ErrorState -> {}
            }
        }
    }
}