package com.project.loveis

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.ItemUserBinding
import com.project.loveis.models.User
import java.util.*

class UserFragment(private val onClick: () -> Unit): Fragment(R.layout.item_user) {
    private val binding: ItemUserBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind(arguments?.getParcelable(UsersViewPagerAdapter.USER), arguments?.getParcelable(UsersViewPagerAdapter.USERS)!!)
    }

  private  fun bind(currentUser:User?, user: User) {
        if (currentUser == null)
            return

        val strings = user.birthday.split('-')
        val birthDate = Calendar.getInstance().apply {
            //set(Calendar.DAY_OF_MONTH, strings[2].toInt() )
            //set(Calendar.MONTH, strings[1].toInt())
            set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
        }
        val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        binding.nameTextView.text = "${user.username}, $age"
        binding.descriptionTextView.text = user.about

      binding.loveIsButton.setOnClickListener {
          findNavController().navigate(R.id.createLoveIsFragment1, Bundle().apply { putInt("user id", user.id) })
      }
      binding.root.setOnClickListener { onClick() }
      binding.chatButton.setOnClickListener { findNavController().navigate(R.id.chatFragment)}
        if (user.verified.not()){
            binding.checkImageView.setImageResource(R.drawable.ic_fail)
            binding.notVerifiedCardView.isVisible = true
            binding.nameTextView.setTextColor(requireContext().resources.getColor(R.color.pink))
        }
        val photoUrl = "https://loveis.scratch.studio" + user.photo
        Glide.with(binding.root)
            .load(photoUrl)
            .into(binding.photoImageView)
      //  val startLatitude = Location.convert(currentUser?.coordinates?.latitude)
      //  val startLongitude = Location.convert(currentUser?.coordinates?.longitude)
        val endLatitude = Location.convert(user.coordinates.latitude)
        val endLongitude = Location.convert(user.coordinates.longitude)
        val results = floatArrayOf(0.0f)
        try{
            Location.distanceBetween(
            61.0,
            32.0,
            endLatitude,
            endLongitude,
            results
        )
            val distance = (results[0] / 1000)
            binding.distanceTextView.text = "${distance.toInt()}км от вас"
        }catch (e: Exception){
            Log.e("Debug", "error = ${e.message}")
            binding.distanceTextView.text = "-"
        }

    }

}