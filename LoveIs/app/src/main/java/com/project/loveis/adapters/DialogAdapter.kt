package com.project.loveis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.R
import com.project.loveis.databinding.ItemDialogBinding
import com.project.loveis.models.Dialog
import com.project.loveis.models.User

class DialogAdapter(private val onClick: (User) -> Unit, private val onAvatarClick: (User) -> Unit) : RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {
    private var dialogs = listOf<Dialog>()

    class DialogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemDialogBinding by viewBinding()

        fun bind(dialog: Dialog, onClick: (User) -> Unit, onAvatarClick: (User) -> Unit){
            binding.root.setOnClickListener {
                onClick(dialog.chatWith)
            }
            binding.avatarImageView.setOnClickListener{
                onAvatarClick(dialog.chatWith)
            }
            binding.nameTextView.text = dialog.chatWith.name
            binding.lastMessageTextView.text = dialog.lastMessage?.content
            val timeStamp = dialog.lastMessage?.timestamp
            val date = timeStamp?.substringBefore('T').orEmpty()
            val day = date.substringAfterLast('-')
            val month = date.substringAfter('-').substringBefore('-')
            val year = date.substringBefore('-')
            val time = timeStamp?.substringAfter('T')?.substringBeforeLast(':')?.substringBeforeLast(':').orEmpty()
            binding.timeTextView.text = "$day.$month.$year $time"

            val photoUrl = "https://loveis.scratch.studio" + dialog.chatWith.photo
            Glide.with(binding.root)
                .load(photoUrl)
                .circleCrop()
                .into(binding.avatarImageView)
        }

    }

    fun updateList(newList: List<Dialog>){
        dialogs = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        return DialogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
          holder.bind(dialogs[position], onClick, onAvatarClick)
    }

    override fun getItemCount(): Int {
        return dialogs.size
    }
}