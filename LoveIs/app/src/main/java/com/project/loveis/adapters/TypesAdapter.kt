package com.project.loveis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.R
import com.project.loveis.databinding.ItemTypeBinding
import com.project.loveis.models.Type

class TypesAdapter(private val onClick: (Long) -> Unit): RecyclerView.Adapter<TypesAdapter.TypeViewHolder>() {
    private var types = listOf<Type>()
    private var checkedTypeId = 1L

    fun updateList(newList: List<Type>){
        types = newList
        notifyDataSetChanged()
    }

    class TypeViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding: ItemTypeBinding by viewBinding()

        fun bind(type: Type, id: Long,  onClick:(Long) -> Unit){
            binding.nameTextView.text = type.name
            if(type.id == id){
                binding.typeCardView.cardElevation = 20f
                binding.typeCardView.strokeWidth = 0
                binding.nameTextView.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.blue, null))
                val iconUrl = "https://loveis.scratch.studio" + type.iconActive
                Glide.with(itemView)
                    .load(iconUrl)
                    .into(binding.iconImageView)
            }else{
                binding.typeCardView.cardElevation = 0f
                binding.typeCardView.strokeWidth = 3
                binding.nameTextView.setTextColor(ResourcesCompat.getColor(itemView.resources, R.color.gray2, null))
                val iconUrl = "https://loveis.scratch.studio" + type.icon
                Glide.with(itemView)
                    .load(iconUrl)
                    .into(binding.iconImageView)
            }
            binding.typeCardView.setOnClickListener {
                    if(type.id != id)
                    onClick(type.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
       return TypeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_type, parent, false))
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.bind(types[position], checkedTypeId){
            checkedTypeId = it
            notifyDataSetChanged()
            onClick(it)
        }
    }

    override fun getItemCount(): Int {
        return types.size
    }
}