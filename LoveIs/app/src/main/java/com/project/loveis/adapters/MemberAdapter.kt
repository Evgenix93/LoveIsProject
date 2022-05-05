package com.project.loveis.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.R
import com.project.loveis.databinding.ItemLoveisEventisMemberBinding
import com.project.loveis.models.User
import java.util.*

class MemberAdapter(private val isLoveIs: Boolean, private val status: String, private val onRemovePerson: (User) -> Unit, private val onClick:(User, User) -> Unit): RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private var items = listOf<User>()
    private lateinit var adminUser: User
    private lateinit var currentUser: User
    private var slotsCount = 0

   inner class MemberViewHolder(view: View, private val isLoveIs: Boolean, val onRemovePerson: (Int) -> Unit): RecyclerView.ViewHolder(view){
        private val binding: ItemLoveisEventisMemberBinding by viewBinding()

        fun bind(member: User?, adminUser: User, currentUser: User){
            if(member == null) {
                binding.personAvatar.setImageResource(R.drawable.empty_avatar)
            }
                binding.emptySlotTextView.isVisible = member == null
                binding.personNameTextView.isVisible = member != null
                binding.personAgeTextView.isVisible = member != null
                binding.removePersonBtn.isVisible = member != null

            if(member == null)
                return

            binding.personNameTextView.text = member.name
            val birthStrings = member.birthday.split("-")
            val calendar = Calendar.getInstance().apply { set(birthStrings[0].toInt(), birthStrings[1].toInt() - 1, birthStrings[2].toInt()) }
            val diffMillis = System.currentTimeMillis() - calendar.timeInMillis
            //calendar.timeInMillis = diffMillis
            val diffYears = ((diffMillis/1000)/86400)/365
            binding.personAgeTextView.text = diffYears.toString()
            val prefix = "https://loveis.scratch.studio/"

            Glide.with(itemView)
                .load(prefix + member.photo)
                .into(binding.personAvatar)

            if(member.phone == currentUser.phone) {
                binding.personNameTextView.text = "Я"
                binding.removePersonBtn.isVisible = false

            }else
                binding.removePersonBtn.isVisible = isLoveIs.not() && currentUser.phone == adminUser.phone

            if(member.phone == adminUser.phone)
                binding.personAgeTextView.text = "Администратор"

            binding.removePersonBtn.isVisible = status == "completed" || status == "canceled" || status == "not_happen"
            binding.removePersonBtn.setOnClickListener {
                onRemovePerson(adapterPosition)
            }

           binding.root.setOnClickListener {onClick(member, currentUser)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loveis_eventis_member, parent, false), isLoveIs) {position ->
            onRemovePerson(items[position])
        }

    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val item = if(position > items.size - 1) null else items[position]
        holder.bind(item, adminUser, currentUser)

    }

    override fun getItemCount(): Int {
        return slotsCount


    }

    fun updateList(newList: List<User>, userAdmin: User, currentUser: User, slotsCount: Int = 2){
        items = newList
        adminUser = userAdmin
        this.currentUser = currentUser
        this.slotsCount = slotsCount
        notifyDataSetChanged()

    }

    fun isUserIn(user: User): Boolean{
        Log.d("debug", items.toString())
        return items.find { it.phone == user.phone } != null

    }
}