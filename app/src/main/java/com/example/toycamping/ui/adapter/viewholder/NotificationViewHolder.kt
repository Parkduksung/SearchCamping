package com.example.toycamping.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.R
import com.example.toycamping.data.model.NotificationItem
import com.example.toycamping.databinding.ItemNotificationBinding
import com.example.toycamping.ext.convertDate

class NotificationViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
) {

    private val binding = ItemNotificationBinding.bind(itemView)

    fun bind(item: NotificationItem, onItemClick: (item: NotificationItem) -> Unit) {
        binding.date.text = item.date?.convertDate()
        binding.title.text = item.title

        itemView.setOnClickListener {
            onItemClick(item)
        }
    }

}