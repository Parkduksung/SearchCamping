package com.example.toycamping.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.R
import com.example.toycamping.databinding.ItemBookmarkBinding
import com.example.toycamping.room.entity.CampingEntity


class BookmarkViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_bookmark, parent, false
    )
) {

    private val binding = ItemBookmarkBinding.bind(itemView)

    fun bind(
        item: CampingEntity,
        itemClickListener: BookmarkListener
    ) {

        binding.apply {
            name.text = item.name
            address.text = item.address

            bookmark.setOnClickListener {
                itemClickListener.getItemClick(item)
            }
        }
    }
}

interface BookmarkListener {
    fun getItemClick(item: CampingEntity)
}