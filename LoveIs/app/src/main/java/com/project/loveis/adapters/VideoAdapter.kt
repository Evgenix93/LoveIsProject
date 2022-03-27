package com.project.loveis.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.R
import com.project.loveis.databinding.ItemVideoBinding
import com.project.loveis.models.Video

class VideoAdapter(private val onClick: (String) -> Unit): RecyclerView.Adapter<VideoAdapter.Holder>() {
    private var videos = listOf<Video>()

    fun updateList(newList: List<Video>){
        videos = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(videos[position], onClick)
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    class Holder(view: View): RecyclerView.ViewHolder(view){
        private val binding: ItemVideoBinding by viewBinding()

        fun bind(video: Video, onClick: (String) -> Unit){
            binding.videoNameTextView.text = video.name

            val videoUrl = "https://loveis.scratch.studio" + video.url
            Glide.with(binding.root)
                .load(videoUrl)
                .into(binding.videoImageView)

            binding.videoImageView.setOnClickListener {onClick(videoUrl)}
        }
    }
}