package com.project.loveis

import android.content.Context
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.adapters.UserPhotoViewPagerAdapter
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.ItemUserBinding
import com.project.loveis.models.User
import com.project.loveis.util.autoCleared
import java.util.*

class UserFragment : Fragment(R.layout.item_user) {
    private val binding: ItemUserBinding by viewBinding()
    private var photoAdapter: UserPhotoViewPagerAdapter by autoCleared()
    private var onClick: () -> Unit = {}
    private var onShare: (Long) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        bind(
            arguments?.getParcelable(UsersViewPagerAdapter.USER),
            arguments?.getParcelable(UsersViewPagerAdapter.USERS)!!
        )
    }

    private fun bind(currentUser: User?, user: User) {
        if (currentUser == null)
            return

        val strings = user.birthday.split('-')
        val birthDate = Calendar.getInstance().apply {
            //set(Calendar.DAY_OF_MONTH, strings[2].toInt() )
            //set(Calendar.MONTH, strings[1].toInt())
            set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
        }
        val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        binding.nameTextView.text = "${user.name}, $age"
        binding.descriptionTextView.text = user.about

        binding.loveIsButton.setOnClickListener {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToCreateLoveIsFragment1(
                    user.id!!,
                    true
                )
            )
        }

        binding.chatButton.setOnClickListener {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToChatFragment(user.id!!, user.name)
            )
        }
        if (user.verified.not()) {
            binding.checkImageView.setImageResource(R.drawable.ic_fail)
            binding.checkImageView.imageTintList = ColorStateList.valueOf(ResourcesCompat.getColor(resources, R.color.pink, null))
            binding.notVerifiedCardView.isVisible = true
            binding.nameTextView.setTextColor(ResourcesCompat.getColor(resources, R.color.pink, null))
        }
        val photoUrls = user.images.map { "https://loveis.scratch.studio" + it.url }
        photoAdapter.updateList(listOf("https://loveis.scratch.studio${user.photo}") + photoUrls)
        val startLatitude = Location.convert(currentUser.coordinates.latitude)
        val startLongitude = Location.convert(currentUser.coordinates.longitude)
        val endLatitude = Location.convert(user.coordinates.latitude)
        val endLongitude = Location.convert(user.coordinates.longitude)
        val results = floatArrayOf(0.0f)
        try {
            Location.distanceBetween(
            startLatitude,
            startLongitude,
            endLatitude,
            endLongitude,
            results
        )
            val distance = (results[0] / 1000)
            binding.distanceTextView.text = "${distance.toInt()}км от вас"
        } catch (e: Exception) {
            Log.e("Debug", "error = ${e.message}")
            binding.distanceTextView.text = "-"
        }

        binding.shareBtn.setOnClickListener {
            onShare(user.id!!)
        }

    }

    private fun initViewPager() {
        photoAdapter = UserPhotoViewPagerAdapter(this) { onClick() }
        binding.photoImageView.adapter = photoAdapter
    }

    companion object{
        fun newInstance(onClick: () -> Unit, onShare: (Long) -> Unit): UserFragment{
            return UserFragment().apply { this.onClick = onClick
            this.onShare = onShare}
        }
    }

}