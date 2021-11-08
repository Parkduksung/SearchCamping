package com.example.toycamping.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.data.model.NotificationItem
import com.example.toycamping.ui.adapter.viewholder.NotificationViewHolder

class NotificationAdapter : RecyclerView.Adapter<NotificationViewHolder>() {

    private val notificationList = mutableListOf<NotificationItem>()

    private lateinit var onItemClick: (item: NotificationItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(parent)

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position], onItemClick)
    }

    override fun getItemCount(): Int =
        notificationList.size

    fun setOnItemClickListener(listener: (item: NotificationItem) -> Unit) {
        onItemClick = listener
    }

    fun getAll(list: List<NotificationItem>) {
        notificationList.addAll(list)
        notifyDataSetChanged()
    }

    fun clear() {
        notificationList.clear()
        notifyDataSetChanged()
    }
}