package com.project.loveis

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.adapters.MemberAdapter
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.FragmentLoveisEventisDetailsBinding
import com.project.loveis.dialogs.CompleteMeetingDialog
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.User
import com.project.loveis.util.MeetingStatus
import com.project.loveis.util.autoCleared
import com.project.loveis.util.toPhotoUrl
import com.project.loveis.viewmodels.LoveIsEveintIsViewModel
import java.util.*


class LoveIsDetailsFragment : Fragment(R.layout.fragment_loveis_eventis_details) {
    private val binding: FragmentLoveisEventisDetailsBinding by viewBinding()
    private val viewModel: LoveIsEveintIsViewModel by viewModels()
    private val args: LoveIsDetailsFragmentArgs by navArgs()
    private var personAdapter: MemberAdapter by autoCleared()
    private lateinit var currentLoveIs: LoveIs


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeNotification()
        initToolbar()
        bind(args.loveIs)
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        initList()
        bindViewModel()
        getLoveIsById(args.loveIsId)

    }

    private fun removeNotification(){
        NotificationManagerCompat.from(requireContext()).cancel(args.loveIsId.toInt())
    }
    private fun bind(loveIs: LoveIs?) {
        if (loveIs == null)
            return
        Log.d("MyDebug", "loveisId = ${loveIs.id}")
        currentLoveIs = loveIs
        getCurrentUserInfo()


        Glide.with(this)
            .load(loveIs.place.photo.toPhotoUrl())
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
        binding.finishBtn.isVisible = diffMillis/1000 >= 3600

        if(args.filterType == MeetingFilterType.INCOMING.value){
            binding.finishBtn.isVisible = true
            binding.closeImageView.isVisible = true
            binding.finishBtn.text = "Принять"
            binding.finishBtn.setOnClickListener {
                viewModel.acceptLoveIs(loveIs.id)
            }
            binding.closeImageView.setOnClickListener {
                viewModel.changeLoveIsStatus(loveIs.id, MeetingStatus.CANCEL)
            }

        }

        if(loveIs.telegramUrl != null){
            binding.materialCardView2.isVisible = true
            binding.materialCardView2.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = loveIs.telegramUrl.toUri()
                }
                if(requireContext().packageManager.resolveActivity(intent, 0) != null)
                    startActivity(intent)
            }
        }
        else binding.materialCardView2.isVisible = false

        if(loveIs.whatsAppUrl != null){
            binding.materialCardView3.isVisible = true
            binding.materialCardView3.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = loveIs.whatsAppUrl.toUri()
                }
                if(requireContext().packageManager.resolveActivity(intent, 0) != null)
                    startActivity(intent)
            }
        }
        else binding.materialCardView3.isVisible = false











    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.setImageResource(R.drawable.ic_share)
            burgerMenu.setOnClickListener {
                viewModel.shareLoveIs(args.loveIs?.id ?: args.loveIsId)
            }
        }
    }

    private fun initList() {
        personAdapter = MemberAdapter(isLoveIs = true, currentLoveIs.status, {}, {user, currentUser ->
            findNavController().navigate(R.id.userFragment, bundleOf(
                UsersViewPagerAdapter.IS_LIST to false,
                UsersViewPagerAdapter.USER to currentUser,
                UsersViewPagerAdapter.USERS to user
            ))
        })
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
                            currentLoveIs.invitingUser,
                            currentLoveIs.invitedUser
                        )
                        personAdapter.updateList(
                            members,
                            userAdmin = currentLoveIs.invitingUser,
                            currentUser = state.user
                        )
                    checkCurrentUserFinishedLoveIs(state.user)


                }
                is State.SuccessState -> findNavController().popBackStack()
                is State.LoveIsSingleMeetingLoadedState -> bind(state.meeting)
                is State.LoadedIntent -> startActivity(state.intent)
                is State.SubsriptionNeededState -> findNavController().navigate(LoveIsDetailsFragmentDirections.actionLoveIsDetailsFragmentToPremiumFragment())
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
                viewModel.completeLoveIs(args.loveIs?.id ?: args.loveIsId)
            else
                viewModel.changeLoveIsStatus(args.loveIs?.id ?: args.loveIsId , MeetingStatus.NOT_HAPPEN)

        }.show(childFragmentManager, null)
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun checkCurrentUserFinishedLoveIs(currentUser: User){
        if((currentLoveIs.invitedUser.phone == currentUser.phone && currentLoveIs.invitedUserComplete)
            || currentLoveIs.invitingUser.phone == currentUser.phone && currentLoveIs.invitingUserComplete)
                binding.finishBtn.isVisible = false


    }

    private fun getLoveIsById(id: Long){
        Log.d("MyDebug", "getLoveIs Id = $id")
        if(id == 0L)
            return
        viewModel.getLoveIsById(id)
    }


    companion object{
        const val LOVE = "love is id"
        const val LOVE_IS_STATUS = "love is status"
    }
}