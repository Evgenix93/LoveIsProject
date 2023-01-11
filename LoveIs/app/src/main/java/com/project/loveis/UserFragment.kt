package com.project.loveis

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.project.loveis.adapters.UserPhotoViewPagerAdapter
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.ItemUserBinding
import com.project.loveis.models.User
import com.project.loveis.util.Gender
import com.project.loveis.util.autoCleared
import java.util.*

class UserFragment : Fragment(R.layout.item_user) {
    private val binding: ItemUserBinding by viewBinding()
    private var photoAdapter: UserPhotoViewPagerAdapter by autoCleared()
    private var onClick: () -> Unit = {}
    private var onShare: (Long) -> Unit = {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewPager()
        bind(
            arguments?.getParcelable(UsersViewPagerAdapter.USER),
            arguments?.getParcelable(UsersViewPagerAdapter.USERS)!!
        )
    }

    private fun bind(currentUser: User?, user: User) {
        if (currentUser == null)
            return
        binding.loveIsButton.isVisible = currentUser.id != user.id
        binding.chatButton.isVisible = currentUser.id != user.id
        val strings = user.birthday.split('-')
        val birthDate = Calendar.getInstance().apply {
            set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
        }
        val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        binding.nameTextView.text = "${user.name}, $age"
        binding.descriptionTextView.text = user.about

        binding.loveIsButton.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.searchFragment)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToCreateLoveIsFragment1(
                    user.id!!,
                    user.gender == Gender.male
                )
            )else
                findNavController().navigate(UserFragmentDirections.actionUserFragmentToCreateLoveIsFragment1(user.id!!, user.gender == Gender.male))
        }

        binding.chatButton.setOnClickListener {
            if(findNavController().currentDestination?.id == R.id.searchFragment)
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToChatFragment(user.id!!, user.name)
            )else
                findNavController().navigate(
                   UserFragmentDirections.actionUserFragmentToChatFragment(user.id!!, user.name)
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
            shareUser(user)
        }

    }

    private fun shareUser(user: User){
        val url =  "https://loveis.ru/user/${user.id}"
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        val clip = ClipData.newPlainText("", url)
        clipboard?.setPrimaryClip(clip)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "https://loveis.ru/user/${user.id}")
            type = "text/plain"
        }
        //if(requireContext().packageManager.resolveActivity(shareIntent, PackageManager.MATCH_DEFAULT_ONLY) != null)
          startActivity(Intent.createChooser(shareIntent, null))
        Toast.makeText(requireContext(), "Ссылка скопирована", Toast.LENGTH_SHORT).show()
    }

    private fun initViewPager() {
        photoAdapter = UserPhotoViewPagerAdapter(this) { onClick() }
        binding.photoImageView.adapter = photoAdapter
    }

    private fun initToolbar(){
        val isList = requireArguments().getBoolean(UsersViewPagerAdapter.IS_LIST, true)
        binding.toolbar.root.isVisible = isList.not()
        binding.toolbar.burgerMenu.isVisible = false
        binding.toolbar.title.text = (arguments?.getParcelable<User>(UsersViewPagerAdapter.USERS)!!).name
        binding.toolbar.logOut.setImageResource(R.drawable.ic_arrow_back)
        binding.toolbar.logOut.setOnClickListener { findNavController().popBackStack() }
    }

    companion object{
        fun newInstance(onClick: () -> Unit, onShare: (Long) -> Unit): UserFragment{
            return UserFragment().apply { this.onClick = onClick
            this.onShare = onShare}
        }
    }

}