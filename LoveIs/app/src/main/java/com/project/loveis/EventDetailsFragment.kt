package com.project.loveis

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
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
import com.project.loveis.models.EventIs
import com.project.loveis.models.User
import com.project.loveis.util.MeetingStatus
import com.project.loveis.util.autoCleared
import com.project.loveis.util.toPhotoUrl
import com.project.loveis.viewmodels.LoveIsEveintIsViewModel
import java.util.*


class EventDetailsFragment : Fragment(R.layout.fragment_loveis_eventis_details) {
    private val binding: FragmentLoveisEventisDetailsBinding by viewBinding()
    private val args: EventDetailsFragmentArgs by navArgs()
    private val viewModel: LoveIsEveintIsViewModel by viewModels()
    private var memberAdapter: MemberAdapter by autoCleared()
    private lateinit var currentUser: User
    private lateinit var currentEventIs: EventIs

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeNotification()
        initToolbar()
        bind(args.eventIs)
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        bindViewModel()
        getEventById()
    }

    private fun removeNotification(){
        NotificationManagerCompat.from(requireContext()).cancel(args.eventId.toInt())
    }

    private fun bind(eventIs: EventIs?) {
        eventIs ?: return
        currentEventIs = eventIs
        getCurrentUser()
        initList()
        Log.d("debug", eventIs.toString())

        Glide.with(this)
            .load(eventIs.place.photo.toPhotoUrl())
            .into(binding.placeImage)
        binding.placeNameTextView.text = eventIs.place.name
        binding.placeAddressTextView.text = eventIs.place.address
        val dateStringList = eventIs.date.split("-")
        binding.dateTextView.text =
            "${dateStringList[2].substringBefore("T")}.${dateStringList[1]}.${dateStringList[0]}"
        val timeString = eventIs.date.substringAfter("T").removeSuffix("+").split(":").subList(0, 2)
            .joinToString(":")


        binding.timeTextView.text = timeString
        binding.loveIsPersonsTextView.text =
            "Участники встречи (${eventIs.participantsCount.toInt() + 1}/${eventIs.personsNumber})"
        binding.personCountTextView.text = (eventIs.participantsCount.toInt() + 1).toString()
        binding.personQuantityTextView.text = " / ${eventIs.personsNumber}"

        Log.d("debug", "participants ${eventIs.participantsCount}")

        if(eventIs.telegramUrl != null){
            binding.materialCardView2.isVisible = true
            binding.materialCardView2.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = eventIs.telegramUrl.toUri()
                }
                if(requireContext().packageManager.resolveActivity(intent, 0) != null)
                    startActivity(intent)
            }
        }
        else binding.materialCardView2.isVisible = false

        if(eventIs.whatsAppUrl != null){
            binding.materialCardView3.isVisible = true
            binding.materialCardView3.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = eventIs.whatsAppUrl.toUri()
                }
                if(requireContext().packageManager.resolveActivity(intent, 0) != null)
                    startActivity(intent)
            }
        }
        else binding.materialCardView3.isVisible = false


    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(com.project.loveis.R.string.event_is)
            logOut.setImageResource(com.project.loveis.R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.setImageResource(com.project.loveis.R.drawable.ic_share)
            burgerMenu.setOnClickListener {
                viewModel.shareEventIs(args.eventIs?.id ?: args.eventId)
            }
        }
    }

    private fun initList() {
        memberAdapter = MemberAdapter(false, currentEventIs.status, { member ->
            viewModel.removeParticipantFromEventIs(currentEventIs.id, member.id!!)
        },{user, currentUser ->
            findNavController().navigate(R.id.userFragment, bundleOf(
                UsersViewPagerAdapter.IS_LIST to false,
                UsersViewPagerAdapter.USER to currentUser,
                UsersViewPagerAdapter.USERS to user
            ))
        })

        with(binding.loveIsMembers) {
            adapter = memberAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.SuccessState -> {
                    showToast("Event Is начат успешно")
                    findNavController().popBackStack()
                }
                is State.LoadingState -> {}
                is State.LoadedEventMembers -> {
                    val members = listOf(currentEventIs.owner) + state.members
                    memberAdapter.updateList(members, currentEventIs.owner, currentUser, slotsCount = currentEventIs.personsNumber)
                    initActionButton()
                    updateMembersCount(members.size)
                }
                is State.LoadedCurrentUser -> {
                    currentUser = state.user
                    viewModel.getEventIsMembers(eventIsId = currentEventIs.id)

                }

                is State.LoadedIntent -> {
                    startIntent(state.intent)
                }
                is State.EventIsSingleMeetingLoadedState -> bind(state.meeting)
                is State.SubsriptionNeededState -> findNavController().navigate(EventDetailsFragmentDirections.actionEventDetailsFragmentToPremiumFragment())
                is State.ErrorState -> {
                    when (state.code) {
                        400 -> showToast("Ошибка")
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }

                }
            }
        })
    }

    private fun getCurrentUser() {
        viewModel.getCurrentUserInfo()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun initActionButton() {
        Log.d("MyDebug", "owner phone = ${currentEventIs.owner.phone}, currentUser phone = ${currentUser.phone}")
        if (currentEventIs.owner.phone == currentUser.phone) {
           // binding.finishBtn.text = "Начать"
            binding.finishBtn.setOnClickListener {
                if(currentEventIs.participantsCount.toInt() + 1 >= 3)
                viewModel.completeEventIs(currentEventIs.id)
                else
                    showToast("Количество участников события не может быть меньше трёх человек")
            }

            val dateStringList = currentEventIs.date.split("-")
            val timeString =
                currentEventIs.date.substringAfter("T").removeSuffix("+").split(":").subList(0, 2)
                    .joinToString(":")

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
            binding.finishBtn.isVisible =
                diffMillis / 1000 >= 0 && currentEventIs.status != MeetingStatus.COMPLETE.value

        } else {
            binding.finishBtn.isVisible = true
            binding.finishBtn.text = "Присоединиться"
            binding.finishBtn.setOnClickListener {
                viewModel.joinEventIs(currentEventIs.id)
            }
            if(memberAdapter.isUserIn(currentUser))
                binding.finishBtn.isVisible = false

        }
    }

    private fun startIntent(intent: Intent){
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Define what your app should do if no activity can handle the intent.
        }
    }

    private fun getEventById(){
        if(args.eventId == 0L)
            return
        viewModel.getEventIsById(args.eventId)
    }

    private fun updateMembersCount(count: Int){
        binding.loveIsPersonsTextView.text =
            "Участники встречи ($count/${currentEventIs.personsNumber})"
        binding.personCountTextView.text = count.toString()

    }


}