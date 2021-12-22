package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.ItemLoveisEventisBinding

class LoveIsEventIsAdapter(val onClick: () -> Unit) : RecyclerView.Adapter<LoveIsEventIsAdapter.LoveIsEventIsViewHolder>() {





    class LoveIsEventIsViewHolder(view: View, onClick: () -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: ItemLoveisEventisBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick()
            }
        }

        fun bind() {
            binding.placeImage.setImageResource(R.drawable.cafe)
            binding.placeNameTextView.text = "Smile"
            binding.placeAddressTextView.text = "Санкт Петербург"
            binding.dateTextView.text = "22.10.21"
            binding.timeTextView.text = "22:30"
            binding.personCountTextView.text = "3"
            binding.personQuantityTextView.text = " / 10"
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoveIsEventIsViewHolder {
        return LoveIsEventIsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loveis_eventis, parent, false)
        ){
            onClick()
        }
    }

    override fun onBindViewHolder(holder: LoveIsEventIsViewHolder, position: Int) {
        holder.bind()


    }

    override fun getItemCount(): Int {
        return 10
    }





}