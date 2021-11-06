package com.example.toycamping.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.ui.adapter.viewholder.SnapViewHolder

class SnapAdapter : RecyclerView.Adapter<SnapViewHolder>() {

    private val snapList = mutableListOf<SnapItem>()

    private lateinit var itemLongClick: (item: SnapItem) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapViewHolder =
        SnapViewHolder(parent)

    override fun onBindViewHolder(holder: SnapViewHolder, position: Int) {
        holder.bind(snapList[position], itemLongClick)
    }

    override fun getItemCount(): Int =
        snapList.size


    fun addAll(list: List<SnapItem>) {
        list.sortedByDescending { it.date }
        snapList.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: SnapItem) {
        snapList.add(0, item)
        notifyItemChanged(0)
    }

    fun delete(item : SnapItem) {
        snapList.remove(item)
        notifyDataSetChanged()
    }

    fun clear() {
        snapList.clear()
        notifyDataSetChanged()
    }

    fun setOnLongClickListener(listener: (item: SnapItem) -> Unit) {
        itemLongClick = listener
    }


}