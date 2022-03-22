package com.project.loveis

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MarginLayoutParamsCompat
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.adapters.MemberAdapter
import com.project.loveis.databinding.FragmentLoveisEventisDetailsBinding
import com.project.loveis.dialogs.CompleteMeetingDialog
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.User
import com.project.loveis.util.MeetingStatus
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.LoveIsViewModel
import java.util.*


class LoveIsDetailsFragment : Fragment(R.layout.fragment_loveis_eventis_details) {
    private val binding: FragmentLoveisEventisDetailsBinding by viewBinding()
    private val viewModel: LoveIsViewModel by viewModels()
    private val args: LoveIsDetailsFragmentArgs by navArgs()
    private var personAdapter: MemberAdapter by autoCleared()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        bind(args.loveIs)
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        initList()
        bindViewModel()
        getCurrentUserInfo()

    }


    private fun bind(loveIs: LoveIs?) {
        if (loveIs == null)
            return
        Glide.with(this)
            .load(loveIs.place.photo)
            .into(binding.placeImage)
        binding.placeNameTextView.text = loveIs.place.name
        binding.placeAddressTextView.text = loveIs.place.address
        val dateStringList = loveIs.date.split("-")
        binding.dateTextView.text =
            "${dateStringList[2].substringBefore("T")}.${dateStringList[1]}.${dateStringList[0]}"
        val timeString = loveIs.date.substringAfter("T").removeSuffix("+").split(":").subList(0, 2)
            .joinToString(":")
        binding.timeTextView.text = timeString
        binding.loveIsPersonsTextView.text = "Участники встречи"
        binding.personIcon.isVisible = false
        binding.personCountTextView.isVisible = false
        binding.personQuantityTextView.isVisible = false

        val calendar = Calendar.getInstance().apply {
                    set(
                        dateStringList[0].toInt(),
                        dateStringList[1].toInt() - 1,
                        dateStringList[2].substringBefore("T").toInt(),
                        timeString.split(":")[0].toInt(),
                        timeString.split(":")[1].toInt()
                    )
                }
                val diffMillis = System.currentTimeMillis() - calendar.timeInMillis
                binding.finishBtn.text = "Завершить"
        binding.finishBtn.setOnClickListener { completeMeeting() }
                if(diffMillis/1000 < 3600){
                    binding.finishBtn.isVisible = false

                }

        if(args.filterType == MeetingFilterType.INCOMING.value){
            binding.finishBtn.isVisible = true
            binding.finishBtn.text = "Принять"
            binding.finishBtn.setOnClickListener {
                viewModel.acceptLoveIs(loveIs.id)
            }
            binding.closeImageView.setOnClickListener {
                viewModel.changeLoveIsStatus(loveIs.id, MeetingStatus.CANCEL)
            }

        }








    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.setImageResource(R.drawable.ic_share)
        }
    }

    private fun initList() {
        personAdapter = MemberAdapter(isLoveIs = args.loveIs != null)
        with(binding.loveIsMembers) {
            adapter = personAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun getCurrentUserInfo() {
        viewModel.getCurrentUserInfo()

    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.LoadedCurrentUser -> {

                    val members = listOf(
                            args.loveIs.invitingUser,
                            args.loveIs.invitedUser
                        )
                        personAdapter.updateList(
                            members,
                            userAdmin = args.loveIs.invitingUser,
                            currentUser = state.user
                        )
                    checkCurrentUserFinishedLoveIs(state.user)


                }
                is State.SuccessState -> findNavController().popBackStack()
                is State.ErrorState -> {
                    when(state.code){
                        400 -> showToast("Ошибка")
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }

                }
                else -> {}
            }

        })
    }

    private fun completeMeeting(){
        CompleteMeetingDialog{ completed ->
            if(completed)
                viewModel.completeLoveIs(args.loveIs.id)
            else
                viewModel.changeLoveIsStatus(args.loveIs.id , MeetingStatus.NOT_HAPPEN)

        }.show(childFragmentManager, null)
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkCurrentUserFinishedLoveIs(currentUser: User){
        if((args.loveIs.invitedUser.name == currentUser.name && args.loveIs.invitedUserComplete)
            || args.loveIs.invitingUser.name == currentUser.name && args.loveIs.invitingUserComplete)
                binding.finishBtn.isVisible = false


    }



}