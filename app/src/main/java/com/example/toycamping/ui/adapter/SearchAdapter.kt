package com.example.toycamping.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.room.CampingEntity
import com.example.toycamping.ui.adapter.viewholder.SearchListener
import com.example.toycamping.ui.adapter.viewholder.SearchViewHolder

class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {

    private val searchList = mutableListOf<CampingEntity>()

    private lateinit var searchListener: SearchListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(parent)

    override fun getItemCount(): Int =
        searchList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) =
        holder.bind(searchList[position], searchListener)

    @SuppressLint("NotifyDataSetChanged")
    fun addAllSearchData(list: List<CampingEntity>) {
        searchList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        searchList.clear()
        notifyDataSetChanged()
    }

    fun setSearchItemClickListener(listener: SearchListener) {
        searchListener = listener
    }
}
