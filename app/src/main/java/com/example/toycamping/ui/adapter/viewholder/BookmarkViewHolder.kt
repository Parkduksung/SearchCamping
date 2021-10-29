package com.example.toycamping.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.R
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.databinding.ItemBookmarkBinding


class BookmarkViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_bookmark, parent, false
    )
) {

    private val binding = ItemBookmarkBinding.bind(itemView)

    fun bind(
        item: CampingItem,
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
    fun getItemClick(item: CampingItem)
}