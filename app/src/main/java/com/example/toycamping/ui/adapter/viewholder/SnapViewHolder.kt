package com.example.toycamping.ui.adapter.viewholder

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.toycamping.R
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.databinding.ItemSnapBinding
import com.example.toycamping.ext.getDate

class SnapViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_snap, parent, false)
) {

    private val binding = ItemSnapBinding.bind(itemView)

    fun bind(item: SnapItem, itemLongClick: (item: SnapItem) -> Unit) {

        with(binding) {
            Glide.with(itemView.context).load(item.image).into(image)
            date.text = item.date?.getDate()
            name.text = item.name
            detail.text = item.detail
        }

        itemView.setOnLongClickListener {
            itemLongClick(item)
            true
        }

    }
}