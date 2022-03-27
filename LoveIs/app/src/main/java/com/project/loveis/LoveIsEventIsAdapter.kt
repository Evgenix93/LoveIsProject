package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.ItemLoveisEventisBinding
import com.project.loveis.models.EventIs
import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.util.toPhotoUrl

class LoveIsEventIsAdapter(val onClick: (LoveIs) -> Unit, val onEventClick: (EventIs) -> Unit, val onAccept: (Long) -> Unit, val onDecline: (Long) -> Unit) : RecyclerView.Adapter<LoveIsEventIsAdapter.LoveIsEventIsViewHolder>() {
    private var items = listOf<LoveIs>()
    private var eventIsItems = listOf<EventIs>()
    private var currentType = MeetingFilterType.ACTIVE
    private var currentEventTypeId = 0L
    private var eventIsItemsNotFiltered = listOf<EventIs>()





    class LoveIsEventIsViewHolder(view: View, onClick: (Int) -> Unit, val onAccept: (Long) -> Unit, val onDecline: (Long) -> Unit) : RecyclerView.ViewHolder(view) {
        private val binding: ItemLoveisEventisBinding by viewBinding()

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(loveIs: LoveIs, type: MeetingFilterType) {
            Glide.with(itemView)
                .load(loveIs.place.photo.toPhotoUrl())
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

        fun bind(eventIs: EventIs){
            Glide.with(itemView)
                .load(eventIs.place.photo.toPhotoUrl())
                .into(binding.placeImage)
            binding.placeNameTextView.text = eventIs.place.name
            binding.placeAddressTextView.text = eventIs.place.address
            val dateStringList = eventIs.date.split("-")
            binding.dateTextView.text = "${dateStringList[2].substringBefore("T")}.${dateStringList[1]}.${dateStringList[0]}"
            val timeString = eventIs.date.substringAfter("T").removeSuffix("+").split(":").subList(0,2).joinToString(":")
            binding.timeTextView.text = timeString
            binding.personCountTextView.text = (eventIs.participantsCount.toInt() + 1).toString()
            binding.personQuantityTextView.text = " / 10"
            binding.personCountTextView.isVisible = true
            binding.personQuantityTextView.isVisible = true
            binding.personIcon.isVisible = true
            binding.accessBtn.isVisible = false
            binding.closeImageView.isVisible = false






        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoveIsEventIsViewHolder {
        return LoveIsEventIsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_loveis_eventis, parent, false),
        { position ->
            if(items.isNotEmpty())
            onClick(items[position])
            else
                onEventClick(eventIsItems[position])
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
        if(items.isNotEmpty())
        holder.bind(items[position], currentType)
        else
            holder.bind(eventIsItems[position])


    }

    override fun getItemCount(): Int {
        return if(items.isNotEmpty())
            items.size
        else eventIsItems.size
    }

    fun updateLoveIsList(newList: List<LoveIs>, type: MeetingFilterType){
        items = newList
        currentType = type
        notifyDataSetChanged()
    }

    fun updateEventIsList(newList: List<EventIs>, type: MeetingFilterType){
        eventIsItemsNotFiltered = newList
        eventIsItems = newList.filter { if(currentEventTypeId != 0L) it.type.id == currentEventTypeId else
        true}
        currentType = type
        notifyDataSetChanged()
    }

    fun setEventTypeId(id: Long){
        currentEventTypeId = id
        eventIsItems = eventIsItemsNotFiltered.filter { if(currentEventTypeId != 0L) it.type.id == currentEventTypeId else true}
        notifyDataSetChanged()
    }





}