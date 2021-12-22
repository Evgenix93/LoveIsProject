package com.project.loveis

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FinishedLoveIsAdapter :
    RecyclerView.Adapter<FinishedLoveIsAdapter.FinishedLoveIsViewHolder>() {

    class FinishedLoveIsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinishedLoveIsViewHolder {
        return FinishedLoveIsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_finished_loveis, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FinishedLoveIsViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }
}