package com.project.loveis

import android.app.PendingIntent
import android.app.Notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.ActivityMainBinding
import com.project.loveis.models.Dialog
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.PushModel
import com.project.loveis.singletones.Network
import com.project.loveis.singletones.NotificationChannels
import com.project.loveis.util.MessagingService
import com.project.loveis.viewmodels.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainActivityViewModel by viewModels()
    private val pushMessageReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            val message = intent?.getParcelableExtra<PushModel>(MessagingService.PUSH_DATA)
            message?.let {
                if(it.type == "push_chat")
                    if(findNavController(R.id.navHostFragment).currentDestination?.id != R.id.chatFragment)
                        onMessageReceived(it.from!!)

                if(it.type.contains("loveis"))
                   viewModel.getLoveIsById(it.meetingId!!)

        }
    }

        }

    private fun unregisterReceiver(){
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(pushMessageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("mylog", "onCreate")
        bindViewModel()
        registerReceiver()



    }

    override fun onStart() {
        super.onStart()
        // findNavController(R.id.navHostFragment).navigate(R.id.registration1Fragment)
        initBottomNavBar()
        initNavController()
        //handleIntent(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver()
    }


    fun onMessageReceived(from: Long){
        viewModel.getMessages(from)
    }

    private fun registerReceiver(){
        LocalBroadcastManager.getInstance(this).registerReceiver(pushMessageReceiver,
        IntentFilter(MessagingService.PUSH_INTENT)
        )
    }

    private fun createLoveIsNotification(loveIs: LoveIs){
        Log.d("MyDebug", "createLoveIS Notification loveIs id = ${loveIs.id}")
        val intent = Intent(this, this::class.java).putExtra(LoveIsDetailsFragment.LOVE, loveIs.id)
            .putExtra(LoveIsDetailsFragment.LOVE_IS_STATUS, loveIs.status)
        Log.d("MyDebug", "getLong extra = ${intent.getLongExtra(LoveIsDetailsFragment.LOVE, 0)}")
        val pendingIntent = PendingIntent.getActivity(this, 123, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, NotificationChannels.IMPORTANT_CHANNEL_ID)
            .setContentTitle("Love Is")
            .setContentText(
                when(loveIs.status){
                    "create" -> "Пользователь ${loveIs.invitingUser.name} прислал вам Love Is"
                    "accept" -> "Пользователь ${loveIs.invitingUser.name} принял ваш Love Is"
                    "cancel" -> "Пользователь ${loveIs.invitingUser.name} отменил ваш Love Is"
                    else -> "Пользователь ${loveIs.invitingUser.name} прислал вам Love Is"
                }
            )
            .setSmallIcon(R.drawable.love_is)
            .setContentIntent(pendingIntent)
            .build()
        NotificationManagerCompat.from(this).notify(loveIs.id.toInt(), notification)
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
            with(binding.notification) {
                notificationIcon.setImageResource(R.drawable.ic_check)
                notificationIcon.drawable.setTint(resources.getColor(R.color.green))
                notificationTextView.setTextColor(resources.getColor(R.color.green))
                notificationTextView.text = "Успех"
                root.isVisible = true
            }
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
        if(intent == null) return
         else if(intent.data != null)
         if(intent.data.toString().contains("event"))
        findNavController(R.id.navHostFragment).navigate(
            ProfileFragmentDirections.actionProfileFragmentToEventDetailsFragment(
                eventId = intent.data?.lastPathSegment!!.toLong()
            ), NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, false).build()
        ) else if(intent.data.toString().contains("user"))
            handleShareUserIntent(intent)
         else if(intent.data.toString().contains("meeting"))
             findNavController(R.id.navHostFragment).navigate(
                 ProfileFragmentDirections.actionProfileFragmentToLoveIsDetailsFragment(
                     loveIsId = intent.data?.lastPathSegment!!.toLong(),
                     filterType = ""
                 ), NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, false).build()
             )

         if(intent.data == null) {
             Log.d("MyDebug", "onLoveIs Intent")
            if (intent.hasExtra(LoveIsDetailsFragment.LOVE))
                handleLoveIsIntent(intent)
        }

         val userId = intent.getLongExtra(MESSAGE_FROM, -1)
         val userName = intent.getStringExtra(DIALOG_NAME)
         Log.d("mylog", userName.toString())
         if(userId != -1L){
             findNavController(R.id.navHostFragment).navigate(ProfileFragmentDirections.actionProfileFragmentToChatFragment(userId, userName.orEmpty()),
             NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, false).build())
         }


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


     fun getAllgcmDevices(){
        lifecycleScope.launch {
            Network.authApi.getGcmDevices()
        }

    }

     private fun createMessageNotification(dialog: Dialog){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(MESSAGE_FROM, dialog.chatWith.id)
        intent.putExtra(DIALOG_NAME, dialog.chatWith.name)

        val pendingIntent = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(this, NotificationChannels.IMPORTANT_CHANNEL_ID)
            .setContentTitle("Пользователь ${dialog.chatWith.name} оставил вам новое текстовое сообщение")
            //.setContentText(dialog.list?.first()?.content)
            .setSmallIcon(R.drawable.ic_baseline_notifications)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(this).notify(dialog.chatWith.id?.toInt()!!, notification)
    }

    private fun bindViewModel(){
        viewModel.state.observe(this, Observer { state ->
            when(state){
                is State.LoadedSingleState -> {
                    if(state.result is Dialog)
                        createMessageNotification(state.result)
                }
                is State.LoveIsSingleMeetingLoadedState -> {
                    createLoveIsNotification(state.meeting)
                }

            }


        })
    }

    companion object{
        const val MESSAGE_FROM = "message_from"
        const val DIALOG_NAME = "dialog_name"
    }

   private fun handleLoveIsIntent(intent: Intent){
       Log.d("MyDebug", "handleLoveIsIntent long extra =  ${intent.getLongExtra(LoveIsDetailsFragment.LOVE, 0)}")
       val type = when(intent.getStringExtra(LoveIsDetailsFragment.LOVE_IS_STATUS)) {
           "create" -> MeetingFilterType.INCOMING
           else -> MeetingFilterType.ACTIVE
       }
        findNavController(R.id.navHostFragment).navigate(R.id.loveIsDetailsFragment, bundleOf("loveIsId" to
              intent.getLongExtra(LoveIsDetailsFragment.LOVE, 0),
        "filterType" to type.value))
    }
}