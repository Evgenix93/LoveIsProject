package com.project.loveis

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.ItemUserBinding
import com.project.loveis.models.User
import java.util.*

class UserAdapter(private val context: Context, private val onClick: () -> Unit, private val onChatClick: () -> Unit, private val onLoveIsClick: (Int) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var userList = emptyList<User>()
    private var currentUser: User? = null

    class UserViewHolder(view: View, onClick:() -> Unit, onChatClick: () -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: ItemUserBinding by viewBinding()
        private val urlPrefix = "https://loveis.scratch.studio"


        init {
            itemView.setOnClickListener { onClick() }
            binding.chatButton.setOnClickListener {
                onChatClick()
            }

        }

        fun debugBind(){
            binding.nameTextView.text = "Jane Cooper, 21"
            binding.distanceTextView.text = "21км от вас"
        }

        fun bind(context: Context, currentUser:User?, user: User, onLoveIsClick: (Int) -> Unit) {
            if (currentUser == null)
                return
            binding.loveIsButton.setOnClickListener {
                onLoveIsClick(user.id)
            }
            val strings = user.birthday.split('-')
            val birthDate = Calendar.getInstance().apply {
                //set(Calendar.DAY_OF_MONTH, strings[2].toInt() )
                //set(Calendar.MONTH, strings[1].toInt())
                set(strings[0].toInt(), strings[1].toInt(), strings[2].toInt())
            }
            val age = Calendar.getInstance().get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
            binding.nameTextView.text = "${user.username}, $age"
            binding.descriptionTextView.text = user.about


            if (user.verified.not()){
                binding.checkImageView.setImageResource(R.drawable.ic_fail)
                binding.notVerifiedCardView.isVisible = true
                binding.nameTextView.setTextColor(context.resources.getColor(R.color.pink))
            }
            val photoUrl = urlPrefix + user.photo
           // Glide.with(itemView)
             //   .load(photoUrl)
              //  .into(binding.photoImageView)
            val startLatitude = Location.convert(currentUser.coordinates.latitude)
            val startLongitude = Location.convert(currentUser.coordinates.longitude)
            val endLatitude = Location.convert(user.coordinates.latitude)
            val endLongitude = Location.convert(user.coordinates.longitude)
            val results = floatArrayOf(0.0f)
            try{Location.distanceBetween(
                61.0,
                32.0,
                endLatitude,
                endLongitude,
                results
            )
                val distance = (results[0] / 1000)
                binding.distanceTextView.text = "${distance}км от вас"
            }catch (e: Exception){
               Log.e("Debug", "error = ${e.message}")
                binding.distanceTextView.text = "-"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false),
            onClick,
            onChatClick
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(context, currentUser, userList[position], onLoveIsClick)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateList(newList: List<User>){
        userList = newList
        notifyDataSetChanged()
    }

    fun updateCurrentUser(newCurrentUser: User){
        currentUser = newCurrentUser
    }

}