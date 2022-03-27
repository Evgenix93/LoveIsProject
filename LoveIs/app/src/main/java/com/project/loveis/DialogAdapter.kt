package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.ItemDialogBinding
import com.project.loveis.models.Dialog
import com.project.loveis.models.DialogsWrapper
import com.project.loveis.models.User

class DialogAdapter(val onClick: (User) -> Unit) : RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {
    private var dialogs = listOf<Dialog>()

    class DialogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemDialogBinding by viewBinding()

        fun bind(dialog: Dialog, onClick: (User) -> Unit){
            binding.root.setOnClickListener {
                onClick(dialog.chatWith)
            }
            binding.nameTextView.text = dialog.chatWith.name
            binding.lastMessageTextView.text = dialog.lastMessage!!.content
            binding.timeTextView.text = dialog.lastMessage.timestamp.substringAfter('T')
                .substringBeforeLast(':').substringBeforeLast(':')

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
          holder.bind(dialogs[position], onClick)
    }

    override fun getItemCount(): Int {
        return dialogs.size
    }
}