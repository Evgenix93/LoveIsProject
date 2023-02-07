package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.ItemPlaceBinding
import com.project.loveis.models.Place

class PlaceAdapter(private val onClick:(Int)->Unit): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    private var places = emptyList<Place>()
    private var chosenPlaceId = 1

   inner class PlaceViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: ItemPlaceBinding by viewBinding()


        fun bind(place:Place){
            itemView.setOnClickListener {
                chosenPlaceId = place.id
                onClick(place.id)
                notifyDataSetChanged()
            }
            if (chosenPlaceId == place.id){
                binding.root.cardElevation = 20f
                binding.root.strokeWidth = 0
            }else{
                binding.root.cardElevation = 0f
                binding.root.strokeWidth = 3
            }
            binding.placeAddressTextView.text = place.address
            binding.placeNameTextView.text = place.name
            val photo = "https://loveis.scratch.studio" + place.photo
            Glide.with(itemView)
                .load(photo)
                .into(binding.placeImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false))

    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updateList(newList: List<Place>){
       places = newList
       notifyDataSetChanged()
    }
}