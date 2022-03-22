package com.project.loveis.adapters

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

class MemberAdapter(val isLoveIs: Boolean): RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private var items = listOf<User>()
    private lateinit var adminUser: User
    private lateinit var currentUser: User

    class MemberViewHolder(view: View, val isLoveIs: Boolean): RecyclerView.ViewHolder(view){
        private val binding: ItemLoveisEventisMemberBinding by viewBinding()


        fun bind(member: User, adminUser: User, currentUser: User){
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

            if(member.name == adminUser.name )
                binding.personAgeTextView.text = "Администратор"

            if(member.name == currentUser.name)
                binding.personNameTextView.text = "Я"

            binding.removePersonBtn.isVisible = isLoveIs.not()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loveis_eventis_member, parent, false), isLoveIs)

    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(items[position], adminUser, currentUser)

    }

    override fun getItemCount(): Int {
        return items.size


    }

    fun updateList(newList: List<User>, userAdmin: User, currentUser: User){
        items = newList
        adminUser = userAdmin
        this.currentUser = currentUser
        notifyDataSetChanged()

    }
}