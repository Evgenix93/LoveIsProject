package com.project.loveis

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.ItemFinishedLoveisBinding
import com.project.loveis.models.EventIs
import com.project.loveis.models.LoveIs

class FinishedLoveIsAdapter :
    RecyclerView.Adapter<FinishedLoveIsAdapter.FinishedLoveIsViewHolder>() {
    private var items = listOf<LoveIs>()
    private var eventItems = listOf<EventIs>()
    private var eventItemsNotFiltered = listOf<EventIs>()
    private var currentEventTypeId = 0L

    class FinishedLoveIsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemFinishedLoveisBinding by viewBinding()

        fun bind(loveIs: LoveIs? = null, eventIs: EventIs? = null){
            when(loveIs?.status ?: eventIs?.status){
                "complete" -> {
                    binding.loveIsStatusImageView.setImageResource(R.drawable.ic_check)
                    binding.loveIsStatusImageView.imageTintList = ColorStateList.valueOf(itemView.context.resources.getColor(R.color.green))
                    binding.loveIsStatusTextView.text = "Состоялось"
                    binding.loveIsStatusTextView.setTextColor(itemView.context.resources.getColor(R.color.green))
                }
                "cancel" -> {
                    binding.loveIsStatusImageView.setImageResource(R.drawable.cross)
                    binding.loveIsStatusImageView.imageTintList = ColorStateList.valueOf(itemView.context.resources.getColor(R.color.orange))
                    binding.loveIsStatusTextView.text = "Отклонено"
                    binding.loveIsStatusTextView.setTextColor(itemView.context.resources.getColor(R.color.orange))


                }
                "not_happen" -> {
                    binding.loveIsStatusImageView.setImageResource(R.drawable.cross)
                    binding.loveIsStatusImageView.imageTintList = ColorStateList.valueOf(itemView.context.resources.getColor(R.color.pink))
                    binding.loveIsStatusTextView.text = "Не состоялось"
                    binding.loveIsStatusTextView.setTextColor(itemView.context.resources.getColor(R.color.pink))


                }

            }

            binding.placeNameTextView.text = loveIs?.place?.name ?: eventIs?.place?.name

            binding.placeAddressTextView.text = loveIs?.place?.address ?: eventIs?.place?.address
            val dateString = loveIs?.date ?: eventIs?.date.orEmpty()
            val dateStringList = dateString.split("-")
            binding.dateTextView.text =
                "${dateStringList[2].substringBefore("T")}.${dateStringList[1]}.${dateStringList[0]}"

            val timeString = dateString.substringAfter("T").removeSuffix("+").split(":").subList(0, 2)
                .joinToString(":")

            binding.timeTextView.text = timeString



        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedLoveIsViewHolder {
        return FinishedLoveIsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finished_loveis, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FinishedLoveIsViewHolder, position: Int) {
        if(items.isNotEmpty())
        holder.bind(loveIs = items[position])
        else
            holder.bind(eventIs = eventItems[position])

    }

    override fun getItemCount(): Int {
        return if(items.isNotEmpty())
            items.size
        else
            eventItems.size
    }

    fun updateList(newList: List<LoveIs>){
        items = newList
        notifyDataSetChanged()
    }

    fun updateEventIsList(newList: List<EventIs>){
        eventItemsNotFiltered = newList
        eventItems = newList.filter { if(currentEventTypeId != 0L) it.type.id == currentEventTypeId
        else true}
        notifyDataSetChanged()
    }

    fun setEventTypeId(id: Long){
        currentEventTypeId = id
        eventItems = eventItemsNotFiltered.filter { if(currentEventTypeId != 0L) it.type.id == currentEventTypeId
        else true}
        notifyDataSetChanged()

    }
}