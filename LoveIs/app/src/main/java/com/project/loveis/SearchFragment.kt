package com.project.loveis


import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
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
    private var usersViewPagerAdapter by autoCleared<UsersViewPagerAdapter>()
    private var currentUser: User? = null
    //private val args: SearchFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("mylog", "onViewCreated args ${arguments?.getLong(USER_ID, -3)}")
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
            usersViewPagerAdapter = UsersViewPagerAdapter(this@SearchFragment, {}, {userId ->
                viewModel.shareUser(userId)

            })
            adapter = usersViewPagerAdapter
            setOnClickListener{
                Log.d("MyDebug", "onClick user viewPager")
                binding.filterCardView.isVisible = false
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageScrollStateChanged(state: Int) {
                    binding.filterCardView.isVisible = false
                }
            })
        }
        binding.root.setOnClickListener{
            binding.filterCardView.isVisible = false
        }
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.StartState -> { viewModel.getCurrentUser() }
                is State.LoadingState -> showLoading(true)
                is State.LoadedSingleState -> {
                    showLoading(false)
                    when(state.result){
                        is User -> {
                            currentUser = state.result
                            if(arguments?.getLong(USER_ID, -1) == null)
                            viewModel.searchUsers()
                            else viewModel.getUserById(arguments?.getLong(USER_ID)!!)
                        }
                        is SearchResult -> {
                            usersViewPagerAdapter = UsersViewPagerAdapter(this@SearchFragment,{binding.filterCardView.isVisible = false}, { userId ->
                                viewModel.shareUser(userId)

                            })
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
                is State.LoadedIntent -> {
                    if(requireContext().packageManager.resolveActivity(state.intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
                        startActivity(state.intent)
                }
                is State.SharedUserLoaded -> {
                    usersViewPagerAdapter.setCurrentUser(currentUser!!)
                    usersViewPagerAdapter.updateList(listOf(state.user))
                }
                is State.ErrorMessageState -> showMessage(state.message)
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
     viewModel.collectFlowAgeFrom(binding.yearEditText1.textChangedFlow())
        viewModel.collectFlowAgeTo(binding.yearEditText2.textChangedFlow())
    }

    private fun showBottomNavBar(){
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
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

    private fun showUser(id: Long){
        viewModel.getUserById(id)
    }

    private fun showMessage(message: String){
        showLoading(false)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val USER_ID = "user_id"
    }

}