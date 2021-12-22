package com.project.loveis


import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentSearchBinding

import com.project.loveis.models.SearchResult
import com.project.loveis.models.User
import com.project.loveis.util.AutoClearedValue
import com.project.loveis.util.textChangedFlow
import com.project.loveis.viewmodels.SearchViewModel
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: SearchViewModel by viewModels()
    private var userAdapter by AutoClearedValue<UserAdapter>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initFilter()
        showBottomNavBar()
        bindViewModel()
    }


    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.search)
            logOut.isVisible = false
            burgerMenu.setImageResource(R.drawable.ic_filter)
            burgerMenu.setOnClickListener{
                binding.filterCardView.isVisible = binding.filterCardView.isVisible.not()
            }
        }
    }

    private fun initList() {
        userAdapter = UserAdapter(requireContext(), onClick = {binding.filterCardView.isVisible = false}, onChatClick = { findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToChatFragment()) },
            onLoveIsClick = {
                findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToCreateLoveIsFragment1(it))
            })
        with(binding.usersList) {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            setOnClickListener{
                binding.filterCardView.isVisible = false
            }
        }

    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.StartState -> {viewModel.getCurrentUser()}
                is State.LoadingState -> showLoading(true)
                is State.LoadedSingleState -> {
                    showLoading(false)
                    when(state.result){
                        is User -> {
                            userAdapter.updateCurrentUser(state.result)
                            viewModel.searchUsers()
                        }
                        is SearchResult -> userAdapter.updateList(state.result.list)
                    }
                }
                is State.ErrorState -> {
                    showLoading(false)
                    when(state.code){
                        0 -> {}
                        400 ->{}
                        404 -> {}
                    }
                }
                else -> {}
            }
        }
    }

    private fun initFilter(){
        binding.maleImageView.setOnClickListener{
            binding.maleImageView.drawable.setTint(resources.getColor(R.color.blue))
            binding.femaleImageView.drawable.setTint(resources.getColor(R.color.gray))
            viewModel.searchUsers(gender = "male")
        }
        binding.femaleImageView.setOnClickListener{
            binding.femaleImageView.drawable.setTint(resources.getColor(R.color.pink))
            binding.maleImageView.drawable.setTint(resources.getColor(R.color.gray))
            viewModel.searchUsers(gender = "female")
        }
     viewModel.collectFlow(binding.yearEditText1.textChangedFlow())
    }

    private fun showBottomNavBar(){
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
    }

    private fun getCurrentUser(){
        viewModel.getCurrentUser()
    }

    private fun clearState(){
        viewModel.clearState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearState()
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }
}