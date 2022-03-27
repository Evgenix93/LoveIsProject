package com.project.loveis


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.FragmentSearchBinding

import com.project.loveis.models.SearchResult
import com.project.loveis.models.User
import com.project.loveis.util.autoCleared
import com.project.loveis.util.textChangedFlow
import com.project.loveis.viewmodels.SearchViewModel

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding()
    private val viewModel: SearchViewModel by viewModels()
    private var userAdapter by autoCleared<UserAdapter>()
    private var usersViewPagerAdapter by autoCleared<UsersViewPagerAdapter>()
    private var currentUser: User? = null

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

        with(binding.usersList) {
            usersViewPagerAdapter = UsersViewPagerAdapter(this@SearchFragment){binding.filterCardView.isVisible = false}
          //  usersViewPagerAdapter.updateList(users)
            adapter = usersViewPagerAdapter
          //  layoutManager = LinearLayoutManager(requireContext())
           // setHasFixedSize(true)
            setOnClickListener{
                Log.d("MyDebug", "onClick viewPager")
                binding.filterCardView.isVisible = false
            }
        }
        binding.root.setOnClickListener{
            binding.filterCardView.isVisible = false
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
                            currentUser = state.result
//                            userAdapter.updateCurrentUser(state.result)
                           // usersViewPagerAdapter.setCurrentUser(state.result)
                            viewModel.searchUsers()
                        }
                        is SearchResult -> {
                            usersViewPagerAdapter = UsersViewPagerAdapter(this@SearchFragment){binding.filterCardView.isVisible = false}
                            binding.usersList.adapter = usersViewPagerAdapter
                            usersViewPagerAdapter.setCurrentUser(currentUser!!)
                            usersViewPagerAdapter.updateList(state.result.list)
                        }
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

    private fun setOnClickLister(){

    }
}