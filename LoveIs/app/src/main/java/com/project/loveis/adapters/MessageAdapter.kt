package com.project.loveis.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.R
import com.project.loveis.databinding.ItemMessageBinding

class MessageAdapter(val context: Context): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View, val context: Context): RecyclerView.ViewHolder(view){
        private val binding: ItemMessageBinding by viewBinding()

        fun bind(){
            when(adapterPosition){
                0 -> {
                    binding.messageCard.isVisible = true
                    binding.messageLeftCard.isVisible = false
                    binding.photoCard.isVisible = false
                    binding.messageTextView.text = "Let's get lunch. How about pizza?"
                }
                1 -> {
                    binding.messageCard.isVisible = false
                    binding.messageLeftCard.isVisible = true
                    binding.messageLeftTextView.text = "Let's get lunch. How about pizza?"
                    binding.photoLeftCard.isVisible = false
                    binding.rightTaleImageView.isVisible = false
                    binding.leftTaleImageView.isVisible = true


                }
                2 -> {
                    binding.messageLeftCard.isVisible = false
                    binding.messageCard.isVisible = true
                    binding.messageTextView.text = "Let's get lunch. How about pizza?"
                    binding.photoCard.isVisible = true
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false), context)

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind()

    }

    override fun getItemCount(): Int {
        return 3

    }

}