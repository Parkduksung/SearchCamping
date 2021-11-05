package com.example.toycamping.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.CampingItem
import com.example.toycamping.databinding.BookmarkFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.ui.adapter.BookmarkAdapter
import com.example.toycamping.ui.adapter.viewholder.BookmarkListener
import com.example.toycamping.viewmodel.BookmarkViewModel
import com.example.toycamping.viewmodel.HomeViewModel

class BookmarkFragment : BaseFragment<BookmarkFragmentBinding>(R.layout.bookmark_fragment),
    BookmarkListener {

    private val bookmarkAdapter by lazy { BookmarkAdapter() }

    private val bookmarkViewModel by viewModels<BookmarkViewModel>()

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun getItemClick(item: CampingItem) {
        homeViewModel.deleteBookmarkItem(item)
    }

    override fun call(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse(number)
        }
        startActivity(intent)
    }

    override fun link(url: String?) {
        url?.let {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initUi() {
        startBookmarkAdapter()
    }

    private fun initViewModel() {
        lifecycle.addObserver(bookmarkViewModel)

        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let {
                onChangedHomeViewState(viewState)
            }

        }

        bookmarkViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? BookmarkViewModel.BookmarkViewState)?.let {
                onChangedBookmarkViewState(viewState)
            }
        }
    }


    private fun onChangedBookmarkViewState(viewState: BookmarkViewModel.BookmarkViewState) {
        when (viewState) {
            is BookmarkViewModel.BookmarkViewState.BookmarkList -> {
                bookmarkAdapter.addAllBookmarkData(viewState.bookmarkList)
            }
            is BookmarkViewModel.BookmarkViewState.Error -> {
                showToast(message = viewState.errorMessage)
            }

            is BookmarkViewModel.BookmarkViewState.EmptyBookmarkList -> {

            }
        }
    }


    private fun onChangedHomeViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {
            is HomeViewModel.HomeViewState.AddBookmarkItem -> {
                bookmarkAdapter.addBookmark(viewState.item)
            }
            is HomeViewModel.HomeViewState.DeleteBookmarkItem -> {
                bookmarkAdapter.deleteBookmark(viewState.item)
            }

            is HomeViewModel.HomeViewState.NotLoginState -> {
                binding.bookmarkRv.isVisible = false
                binding.containerNotLoginBookmark.isVisible = true
                bookmarkAdapter.clear()
            }

            is HomeViewModel.HomeViewState.LoginState -> {
                binding.bookmarkRv.isVisible = true
                binding.containerNotLoginBookmark.isVisible = false
                bookmarkViewModel.getAllBookmark()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarVisibility(false)
    }

    private fun startBookmarkAdapter() {
        binding.bookmarkRv.run {
            adapter = bookmarkAdapter
            bookmarkAdapter.clear()
            layoutManager = LinearLayoutManager(requireContext())
            bookmarkAdapter.setBookmarkItemClickListener(this@BookmarkFragment)
        }
    }

}