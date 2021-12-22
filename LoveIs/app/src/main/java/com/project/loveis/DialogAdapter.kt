package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class DialogAdapter(val onClick: () -> Unit) : RecyclerView.Adapter<DialogAdapter.DialogViewHolder>() {

    class DialogViewHolder(view: View, onClick: () -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                onClick()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        return DialogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        ){
            onClick()
        }
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }
}