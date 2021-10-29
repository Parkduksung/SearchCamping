package com.example.toycamping.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.ui.adapter.viewholder.BookmarkListener
import com.example.toycamping.ui.adapter.viewholder.BookmarkViewHolder

class BookmarkAdapter : RecyclerView.Adapter<BookmarkViewHolder>() {

    private val _bookmarkSet = mutableSetOf<CampingItem>()
    private val bookmarkList get() =  _bookmarkSet.toList()

    private lateinit var bookmarkListener: BookmarkListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder =
        BookmarkViewHolder(parent)

    override fun getItemCount(): Int =
        bookmarkList.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) =
        holder.bind(bookmarkList[position], bookmarkListener)

    @SuppressLint("NotifyDataSetChanged")
    fun addAllBookmarkData(list: List<CampingItem>) {
        _bookmarkSet.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        _bookmarkSet.clear()
        notifyDataSetChanged()
    }

    fun setBookmarkItemClickListener(listener: BookmarkListener) {
        bookmarkListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteBookmark(item: CampingItem) {
        _bookmarkSet.removeAll(bookmarkList.filter { it.name == item.name })
        notifyDataSetChanged()
    }

    fun addBookmark(item: CampingItem) {
        _bookmarkSet.add(item)
        notifyItemChanged(bookmarkList.lastIndex)
    }


}
