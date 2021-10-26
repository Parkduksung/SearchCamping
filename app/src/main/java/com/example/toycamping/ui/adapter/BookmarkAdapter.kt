package com.example.toycamping.ui.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.toycamping.room.entity.CampingEntity
import com.example.toycamping.ui.adapter.viewholder.BookmarkListener
import com.example.toycamping.ui.adapter.viewholder.BookmarkViewHolder

class BookmarkAdapter : RecyclerView.Adapter<BookmarkViewHolder>() {

    private val bookmarkList = mutableListOf<CampingEntity>()

    private lateinit var bookmarkListener: BookmarkListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder =
        BookmarkViewHolder(parent)

    override fun getItemCount(): Int =
        bookmarkList.size

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) =
        holder.bind(bookmarkList[position], bookmarkListener)

    @SuppressLint("NotifyDataSetChanged")
    fun addAllBookmarkData(list: List<CampingEntity>) {
        this.bookmarkList.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        bookmarkList.clear()
        notifyDataSetChanged()
    }

    fun setBookmarkItemClickListener(listener: BookmarkListener) {
        bookmarkListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteBookmark(item: CampingEntity) {
        bookmarkList.removeAll(bookmarkList.filter { it.name == item.name })
        notifyDataSetChanged()
    }

    fun addBookmark(item: CampingEntity) {
        bookmarkList.add(item)
        notifyItemChanged(bookmarkList.lastIndex)
    }


}
