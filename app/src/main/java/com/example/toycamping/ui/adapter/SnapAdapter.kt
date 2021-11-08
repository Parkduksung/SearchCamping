package com.example.toycamping.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.ui.adapter.viewholder.SnapViewHolder

class SnapAdapter : RecyclerView.Adapter<SnapViewHolder>() {

    private val snapSet = mutableSetOf<SnapItem>()
    private val snapList get() = snapSet.toList().sortedByDescending { it.date }

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
        snapSet.addAll(list)
        notifyDataSetChanged()
    }

    fun add(item: SnapItem) {
        snapSet.add(item)
        notifyItemChanged(0)
    }

    fun delete(item: SnapItem) {
        snapSet.remove(item)
        notifyDataSetChanged()
    }

    fun clear() {
        snapSet.clear()
        notifyDataSetChanged()
    }

    fun setOnLongClickListener(listener: (item: SnapItem) -> Unit) {
        itemLongClick = listener
    }


}