package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.ItemLoveisEventisBinding
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType

class LoveIsEventIsAdapter(val onClick: (LoveIs) -> Unit, val onAccept: (Long) -> Unit, val onDecline: (Long) -> Unit) : RecyclerView.Adapter<LoveIsEventIsAdapter.LoveIsEventIsViewHolder>() {
    private var items = listOf<LoveIs>()
    private var currentType = MeetingFilterType.ACTIVE





    class LoveIsEventIsViewHolder(view: View, onClick: (Int) -> Unit, val onAccept: (Long) -> Unit, val onDecline: (Long) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: ItemLoveisEventisBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(loveIs: LoveIs, type: MeetingFilterType) {
            Glide.with(itemView)
                .load(loveIs.place.photo)
                .into(binding.placeImage)
            binding.placeNameTextView.text = loveIs.place.name
            binding.placeAddressTextView.text = loveIs.place.address
            val dateStringList = loveIs.date.split("-")
            binding.dateTextView.text = "${dateStringList[2].substringBefore("T")}.${dateStringList[1]}.${dateStringList[0]}"
            val timeString = loveIs.date.substringAfter("T").removeSuffix("+").split(":").subList(0,2).joinToString(":")
            binding.timeTextView.text = timeString
            //binding.personCountTextView.text = "3"
            //binding.personQuantityTextView.text = " / 10"
            binding.personCountTextView.isVisible = false
            binding.personQuantityTextView.isVisible = false
            binding.personIcon.isVisible = false

            when(type){
                MeetingFilterType.ACTIVE -> {
                    binding.accessBtn.isVisible = false
                    binding.closeImageView.isVisible = false
                }
                MeetingFilterType.MY -> {
                   binding.accessBtn.isVisible = false
                   binding.closeImageView.isVisible = false
                }
                MeetingFilterType.INCOMING -> {
                    binding.accessBtn.isVisible = true
                    binding.closeImageView.isVisible = true
                }

            }

            binding.accessBtn.setOnClickListener {
                onAccept(loveIs.id)
            }
            binding.closeImageView.setOnClickListener {
                onDecline(loveIs.id)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoveIsEventIsViewHolder {
        return LoveIsEventIsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loveis_eventis, parent, false),
        { position ->
            onClick(items[position])
        },
            { loveIsId ->
                onAccept(loveIsId)

            },
            { loveIsId ->
                onDecline(loveIsId)

            }
        )
    }

    override fun onBindViewHolder(holder: LoveIsEventIsViewHolder, position: Int) {
        holder.bind(items[position], currentType)


    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newList: List<LoveIs>, type: MeetingFilterType){
        items = newList
        currentType = type
        notifyDataSetChanged()
    }





}