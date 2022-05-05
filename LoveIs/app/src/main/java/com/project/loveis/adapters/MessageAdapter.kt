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
import com.project.loveis.util.toPhotoUrl

class MessageAdapter(val context: Context, private val userId: Long, val onPhotoClick: (String) -> Unit): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messages = listOf<Message>()

    class MessageViewHolder(view: View, val context: Context, val onPhotoClick: (String) -> Unit): RecyclerView.ViewHolder(view){
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
            binding.messageTextView.isVisible = true

            binding.photoCard.isVisible = false
            binding.photoCard2.isVisible = false
            binding.photoCard3.isVisible = false
            binding.photoCard4.isVisible = false
            binding.photoCard5.isVisible = false
            binding.photoCard6.isVisible = false
            binding.photoCard7.isVisible = false
            binding.photoCard8.isVisible = false
            binding.photoCard9.isVisible = false
            binding.photoCard10.isVisible = false

            binding.photoLeftCard.isVisible = false
            binding.photoLeftCard2.isVisible = false
            binding.photoLeftCard3.isVisible = false
            binding.photoLeftCard4.isVisible = false
            binding.photoLeftCard5.isVisible = false
            binding.photoLeftCard6.isVisible = false
            binding.photoLeftCard7.isVisible = false
            binding.photoLeftCard8.isVisible = false
            binding.photoLeftCard9.isVisible = false
            binding.photoLeftCard10.isVisible = false
        }

        private fun bindMyMediaMessage(message: Message.MyMediaMessage) {
            binding.leftTaleImageView.isVisible = false
            binding.rightTaleImageView.isVisible = true
            binding.messageCard.isVisible = true
            binding.messageLeftCard.isVisible = false
            //binding.photoCard.isVisible = true
            binding.messageTextView.text = message.text
            binding.messageTextView.isVisible = message.text != null

            binding.photoCard.isVisible = false
            binding.photoCard2.isVisible = false
            binding.photoCard3.isVisible = false
            binding.photoCard4.isVisible = false
            binding.photoCard5.isVisible = false
            binding.photoCard6.isVisible = false
            binding.photoCard7.isVisible = false
            binding.photoCard8.isVisible = false
            binding.photoCard9.isVisible = false
            binding.photoCard10.isVisible = false

            binding.photoLeftCard.isVisible = false
            binding.photoLeftCard2.isVisible = false
            binding.photoLeftCard3.isVisible = false
            binding.photoLeftCard4.isVisible = false
            binding.photoLeftCard5.isVisible = false
            binding.photoLeftCard6.isVisible = false
            binding.photoLeftCard7.isVisible = false
            binding.photoLeftCard8.isVisible = false
            binding.photoLeftCard9.isVisible = false
            binding.photoLeftCard10.isVisible = false

            message.urls.forEachIndexed { index, url ->
                //binding.contentList.addView(binding.photoCard)
                when(index){
                    0 -> {
                        binding.photoCard.isVisible = true
                        binding.photoCard.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView)
                    }
                    1 -> {
                        binding.photoCard2.isVisible = true
                        binding.photoCard2.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView2)
                    }
                    2 -> {
                        binding.photoCard3.isVisible = true
                        binding.photoCard3.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView3)
                    }
                    3 -> {
                        binding.photoCard4.isVisible = true
                        binding.photoCard4.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView4)
                    }
                    4 -> {
                        binding.photoCard5.isVisible = true
                        binding.photoCard5.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView5)
                    }
                    5 -> {
                        binding.photoCard6.isVisible = true
                        binding.photoCard6.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView6)
                    }
                    6 -> {
                        binding.photoCard7.isVisible = true
                        binding.photoCard7.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView7)
                    }
                    7 -> {
                        binding.photoCard8.isVisible = true
                        binding.photoCard8.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView8)
                    }
                    8 -> {
                        binding.photoCard9.isVisible = true
                        binding.photoCard9.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView9)
                    }
                    9 -> {
                        binding.photoCard10.isVisible = true
                        binding.photoCard10.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                        Glide.with(binding.root)
                            .load(url.toPhotoUrl())
                            .into(binding.photoImageView10)
                    }
                }

                //val mediaUrl = "https://loveis.scratch.studio" + it
                //Log.d("MyDebug", "url = $mediaUrl")


            }
        }

        private fun bindCompanionMessage(message: Message.CompanionMessage){
            binding.leftTaleImageView.isVisible = true
            binding.rightTaleImageView.isVisible = false
            binding.messageCard.isVisible = false
            binding.messageLeftCard.isVisible = true
            binding.photoLeftCard.isVisible = false
            binding.messageLeftTextView.text = message.text
            binding.messageLeftTextView.isVisible = true

            binding.photoCard.isVisible = false
            binding.photoCard2.isVisible = false
            binding.photoCard3.isVisible = false
            binding.photoCard4.isVisible = false
            binding.photoCard5.isVisible = false
            binding.photoCard6.isVisible = false
            binding.photoCard7.isVisible = false
            binding.photoCard8.isVisible = false
            binding.photoCard9.isVisible = false
            binding.photoCard10.isVisible = false

            binding.photoLeftCard.isVisible = false
            binding.photoLeftCard2.isVisible = false
            binding.photoLeftCard3.isVisible = false
            binding.photoLeftCard4.isVisible = false
            binding.photoLeftCard5.isVisible = false
            binding.photoLeftCard6.isVisible = false
            binding.photoLeftCard7.isVisible = false
            binding.photoLeftCard8.isVisible = false
            binding.photoLeftCard9.isVisible = false
            binding.photoLeftCard10.isVisible = false
        }

       private fun bindCompanionMediaMessage(message: Message.CompanionMediaMessage){
           binding.leftTaleImageView.isVisible = true
           binding.rightTaleImageView.isVisible = false
           binding.messageCard.isVisible = false
           binding.messageLeftCard.isVisible = true
           //binding.photoLeftCard.isVisible = true
           binding.messageLeftTextView.text = message.text
           binding.messageLeftTextView.isVisible = message.text != null

           binding.photoCard.isVisible = false
           binding.photoCard2.isVisible = false
           binding.photoCard3.isVisible = false
           binding.photoCard4.isVisible = false
           binding.photoCard5.isVisible = false
           binding.photoCard6.isVisible = false
           binding.photoCard7.isVisible = false
           binding.photoCard8.isVisible = false
           binding.photoCard9.isVisible = false
           binding.photoCard10.isVisible = false

           binding.photoLeftCard.isVisible = false
           binding.photoLeftCard2.isVisible = false
           binding.photoLeftCard3.isVisible = false
           binding.photoLeftCard4.isVisible = false
           binding.photoLeftCard5.isVisible = false
           binding.photoLeftCard6.isVisible = false
           binding.photoLeftCard7.isVisible = false
           binding.photoLeftCard8.isVisible = false
           binding.photoLeftCard9.isVisible = false
           binding.photoLeftCard10.isVisible = false

           message.urls.forEachIndexed { index, url ->
               //binding.contentList.addView(binding.photoCard)
               when(index){
                   0 -> {
                       binding.photoLeftCard.isVisible = true
                       binding.photoLeftCard.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView)
                   }
                   1 -> {
                       binding.photoLeftCard2.isVisible = true
                       binding.photoLeftCard2.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView2)
                   }
                   2 -> {
                       binding.photoLeftCard3.isVisible = true
                       binding.photoLeftCard3.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView3)
                   }
                   3 -> {
                       binding.photoLeftCard4.isVisible = true
                       binding.photoLeftCard4.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView4)
                   }
                   4 -> {
                       binding.photoLeftCard5.isVisible = true
                       binding.photoLeftCard5.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView5)
                   }
                   5 -> {
                       binding.photoLeftCard6.isVisible = true
                       binding.photoLeftCard6.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView6)
                   }
                   6 -> {
                       binding.photoLeftCard7.isVisible = true
                       binding.photoLeftCard7.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView7)
                   }
                   7 -> {
                       binding.photoLeftCard8.isVisible = true
                       binding.photoLeftCard8.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView8)
                   }
                   8 -> {
                       binding.photoLeftCard9.isVisible = true
                       binding.photoLeftCard9.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView9)
                   }
                   9 -> {
                       binding.photoLeftCard10.isVisible = true
                       binding.photoLeftCard10.setOnClickListener { onPhotoClick(url.toPhotoUrl()) }
                       Glide.with(binding.root)
                           .load(url.toPhotoUrl())
                           .into(binding.photoLeftImageView10)
                   }
               }

               //val mediaUrl = "https://loveis.scratch.studio" + it
               //Log.d("MyDebug", "url = $mediaUrl")


           }


           //val mediaUrl = "https://loveis.scratch.studio" + message.urls
           //Glide.with(binding.root)
             //  .load(mediaUrl)
               //.into(binding.photoLeftImageView)
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
                    Message.MyMessage(it.content!!)
                else
                    Message.MyMediaMessage(it.content, it.attachments.map { photoMap -> photoMap["url"]!!})
            else
                if(it.attachments.isEmpty())
                    Message.CompanionMessage(it.content!!)
                else
                    Message.CompanionMediaMessage(it.content, it.attachments.map { photoMap -> photoMap["url"]!!})
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false), context){ uri ->
            onPhotoClick(uri)

        }

    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])

    }

    override fun getItemCount(): Int {
        return messages.size
    }

}