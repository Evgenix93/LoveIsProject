package com.project.loveis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



    }

    override fun onStart() {
        super.onStart()
       // findNavController(R.id.navHostFragment).navigate(R.id.registration1Fragment)
        initBottomNavBar()

    }


    private fun initBottomNavBar(){
        showEnabledBtn(binding.bottomNavBar.navProfile)

        binding.bottomNavBar.navDialog.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.dialogsFragment)
            showEnabledBtn(binding.bottomNavBar.navDialog)
        }

        binding.bottomNavBar.navProfile.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.profileFragment)
            showEnabledBtn(binding.bottomNavBar.navProfile)
        }

        binding.bottomNavBar.navLoveIs.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.loveIsFragment)
            showEnabledBtn(binding.bottomNavBar.navLoveIs)
        }

        binding.bottomNavBar.navEvents.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.eventsFragment)
            showEnabledBtn(binding.bottomNavBar.navEvents)
        }

        binding.bottomNavBar.navSearch.setOnClickListener {
            findNavController(R.id.navHostFragment).navigate(R.id.searchFragment)
            showEnabledBtn(binding.bottomNavBar.navSearch)
        }
    }

    fun hideBottomNavigationBar(hide: Boolean){
        binding.bottomNavBar.root.isVisible = !hide
    }

    fun showErrorNotification(){
        CoroutineScope(Dispatchers.Main).launch {
            with(binding.notification) {
                notificationIcon.setImageResource(R.drawable.cross)
                notificationIcon.drawable.setTint(resources.getColor(R.color.pink))
                notificationTextView.setTextColor(resources.getColor(R.color.pink))
                notificationTextView.text = "Ошибка"
                root.isVisible = true
            }
            delay(2000)
            binding.notification.root.isVisible = false
        }
    }

    fun showSuccessNotification(){
        CoroutineScope(Dispatchers.Main).launch {
            binding.notification.root.isVisible = true
            delay(2000)
            binding.notification.root.isVisible = false
        }
    }

    private fun showEnabledBtn(button: AppCompatButton){
        with(binding.bottomNavBar.navProfile) {
            compoundDrawables[1].alpha = 100
            setTextColor(resources.getColor(R.color.blue2))
        }
        with(binding.bottomNavBar.navDialog) {
            compoundDrawables[1].alpha = 100
            setTextColor(resources.getColor(R.color.blue2))
        }
        with(binding.bottomNavBar.navLoveIs) {
            compoundDrawables[1].alpha = 100
            setTextColor(resources.getColor(R.color.blue2))
        }
        with(binding.bottomNavBar.navEvents) {
            compoundDrawables[1].alpha = 100
            setTextColor(resources.getColor(R.color.blue2))
        }
        with(binding.bottomNavBar.navSearch) {
            compoundDrawables[1].alpha = 100
            setTextColor(resources.getColor(R.color.blue2))
        }

        with(button){
            compoundDrawables[1].alpha = 255
            setTextColor(resources.getColor(R.color.blue))
        }

    }
}