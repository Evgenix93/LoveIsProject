package com.project.loveis

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.ItemFinishedLoveisBinding
import com.project.loveis.models.LoveIs

class FinishedLoveIsAdapter :
    RecyclerView.Adapter<FinishedLoveIsAdapter.FinishedLoveIsViewHolder>() {
    private var items = listOf<LoveIs>()

    class FinishedLoveIsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemFinishedLoveisBinding by viewBinding()

        fun bind(loveIs: LoveIs){
            when(loveIs.status){
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

            binding.placeAddressTextView.text = loveIs.place.address
            binding.dateTextView.text = loveIs.date
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedLoveIsViewHolder {
        return FinishedLoveIsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finished_loveis, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FinishedLoveIsViewHolder, position: Int) {
        holder.bind(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newList: List<LoveIs>){
        items = newList
        notifyDataSetChanged()
    }
}