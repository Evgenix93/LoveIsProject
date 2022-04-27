package com.project.loveis

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
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
        Log.d("mylog", "onCreate")



    }

    override fun onStart() {
        super.onStart()
        // findNavController(R.id.navHostFragment).navigate(R.id.registration1Fragment)
        initBottomNavBar()
        initNavController()
        //handleIntent(intent)


    }


    private fun initBottomNavBar() {
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

    fun hideBottomNavigationBar(hide: Boolean) {
        binding.bottomNavBar.root.isVisible = !hide
    }

    fun showErrorNotification() {
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

    fun showSuccessNotification() {
        CoroutineScope(Dispatchers.Main).launch {
            binding.notification.root.isVisible = true
            delay(2000)
            binding.notification.root.isVisible = false
        }
    }

    private fun showEnabledBtn(button: AppCompatButton) {
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

        with(button) {
            compoundDrawables[1].alpha = 255
            setTextColor(resources.getColor(R.color.blue))
        }

    }

    private fun changeStatusBarColor(colorRes: Int) {
        window?.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            setStatusBarColor(resources.getColor(colorRes))
        }
    }

    private fun initNavController() {
        findNavController(R.id.navHostFragment).addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id != R.id.splashScreenFragment) {
                changeStatusBarColor(R.color.white)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    @Suppress("DEPRECATION")
                    window.decorView.systemUiVisibility =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        } else {
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        }

                }
                //window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            } else {
                changeStatusBarColor(R.color.blue)
                window.decorView.systemUiVisibility = 0
            }
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

     fun onLogined() {
        intent?.data ?: return
         if(intent.data.toString().contains("event"))
        findNavController(R.id.navHostFragment).navigate(
            ProfileFragmentDirections.actionProfileFragmentToEventDetailsFragment(
                eventId = intent.data?.lastPathSegment!!.toLong()
            ), NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, false).build()
        ) else if(intent.data.toString().contains("user"))
            handleShareUserIntent(intent)
         else
             findNavController(R.id.navHostFragment).navigate(
                 ProfileFragmentDirections.actionProfileFragmentToLoveIsDetailsFragment(
                     loveIsId = intent.data?.lastPathSegment!!.toLong(),
                     filterType = ""
                 ), NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, false).build()
             )


         intent = null
    }

    private fun handleShareUserIntent(intent: Intent?){
        Log.d("mylog", "handle intent")
        intent ?: return
        if(intent.data.toString().contains("user").not())
            return
        val userId = intent.data?.lastPathSegment
        Log.d("mylog", intent.data?.lastPathSegment.toString().toLong().toString())
        userId?.let {
        Log.d("mylog", "userId $it")
            findNavController(R.id.navHostFragment)
            .navigate(R.id.searchFragment, bundleOf(  SearchFragment.USER_ID to it.toLong()) )  }
    }
}