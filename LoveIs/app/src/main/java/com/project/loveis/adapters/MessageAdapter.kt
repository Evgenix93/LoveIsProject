package com.project.loveis.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.R
import com.project.loveis.databinding.ItemMessageBinding
import com.project.loveis.models.ChatMessage
import com.project.loveis.models.Message

class MessageAdapter(val context: Context, private val userId: Long): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messages = listOf<Message>()

    class MessageViewHolder(view: View, val context: Context): RecyclerView.ViewHolder(view){
        private val binding: ItemMessageBinding by viewBinding()

        fun bind(message: Message){
            when (message) {
               is Message.MyMessage -> bindMyMessage(message)
               is Message.MyMediaMessage -> bindMyMediaMessage(message)
               is Message.CompanionMessage -> bindCompanionMessage(message)
               is Message.CompanionMediaMessage -> bindCompanionMediaMessage(message)
            }
           /* when(adapterPosition){
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
            }*/
        }

        private fun bindMyMessage(message: Message.MyMessage){
            binding.leftTaleImageView.isVisible = false
            binding.rightTaleImageView.isVisible = true
            binding.messageCard.isVisible = true
            binding.messageLeftCard.isVisible = false
            binding.photoCard.isVisible = false
            binding.messageTextView.text = message.text
        }

        private fun bindMyMediaMessage(message: Message.MyMediaMessage){
            binding.leftTaleImageView.isVisible = false
            binding.rightTaleImageView.isVisible = true
            binding.messageCard.isVisible = true
            binding.messageLeftCard.isVisible = false
            binding.photoCard.isVisible = true
            binding.messageTextView.text = message.text

            val mediaUrl = "https://loveis.scratch.studio" + message.url
            Log.d("MyDebug", "url = $mediaUrl")
            Glide.with(binding.root)
                .load(mediaUrl)
                .into(binding.photoImageView)
        }

        private fun bindCompanionMessage(message: Message.CompanionMessage){
            binding.leftTaleImageView.isVisible = true
            binding.rightTaleImageView.isVisible = false
            binding.messageCard.isVisible = false
            binding.messageLeftCard.isVisible = true
            binding.photoLeftCard.isVisible = false
            binding.messageLeftTextView.text = message.text
        }

       private fun bindCompanionMediaMessage(message: Message.CompanionMediaMessage){
           binding.leftTaleImageView.isVisible = true
           binding.rightTaleImageView.isVisible = false
           binding.messageCard.isVisible = false
           binding.messageLeftCard.isVisible = true
           binding.photoLeftCard.isVisible = true
           binding.messageLeftTextView.text = message.text

           val mediaUrl = "https://loveis.scratch.studio" + message.url
           Glide.with(binding.root)
               .load(mediaUrl)
               .into(binding.photoLeftImageView)
       }
    }

    fun updateList(newList: List<ChatMessage>){
        messages = convertChatMessagesToMessages(newList)
        notifyDataSetChanged()
    }

    private fun convertChatMessagesToMessages(chatMessages: List<ChatMessage>): List<Message>{
        return chatMessages.map{
            if(it.user.id != userId)
                if(it.attachments.isEmpty())
                    Message.MyMessage(it.content)
                else
                    Message.MyMediaMessage(it.content, it.attachments[0]["url"]!!)
            else
                if(it.attachments.isEmpty())
                    Message.CompanionMessage(it.content)
                else
                    Message.CompanionMediaMessage(it.content, it.attachments[0]["url"]!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false), context)

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])

    }

    override fun getItemCount(): Int {
        return messages.size
    }

}