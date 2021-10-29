package com.example.toycamping.ui.search

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toycamping.R
import com.example.toycamping.base.BaseActivity
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.ActivitySearchBinding
import com.example.toycamping.databinding.ViewSearchBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.room.CampingEntity
import com.example.toycamping.ui.adapter.SearchAdapter
import com.example.toycamping.ui.adapter.viewholder.SearchListener
import com.example.toycamping.viewmodel.SearchViewModel

class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search),
    SearchListener {

    private val searchAdapter by lazy { SearchAdapter() }

    private val searchViewModel by viewModels<SearchViewModel>()

    private lateinit var viewSearchBinding: ViewSearchBinding

    override fun getItemClick(item: CampingEntity) {
        showToast(message = item.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initUi()
        initViewModel()

    }

    private fun settingSearchView() {
        viewSearchBinding = ViewSearchBinding.inflate(layoutInflater)

        setCustomView(viewSearchBinding.root, true)

        with(viewSearchBinding) {
            viewModel = searchViewModel

            editView.apply {
                setOnEditorActionListener { _, actionId, _ ->
                    if (text.isNotEmpty() && actionId == EditorInfo.IME_ACTION_SEARCH) {
                        searchViewModel.search()
                        true
                    } else {
                        false
                    }
                }
                doOnTextChanged { text, _, _, _ ->
                    clearButton.isVisible = !text.isNullOrEmpty()
                }
            }

        }
        searchViewModel.resetSearchView()
    }


    override fun onResume() {
        super.onResume()
        setNavigationIcon(R.drawable.ic_back)
        setToolbarVisibility(true)
    }

    private fun initViewModel() {

        binding.viewModel = searchViewModel

        searchViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? SearchViewModel.SearchViewState)?.let { onChangedViewState(viewState) }
        }
    }

    private fun onChangedViewState(viewState: SearchViewModel.SearchViewState) {
        when (viewState) {

            is SearchViewModel.SearchViewState.Error -> {
                showToast(message = viewState.message)
            }

            is SearchViewModel.SearchViewState.ShowProgress -> {
                binding.progressbar.bringToFront()
                binding.progressbar.isVisible = true
            }

            is SearchViewModel.SearchViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }

            is SearchViewModel.SearchViewState.GetSearchData -> {
                binding.searchRv.isVisible = true
                binding.guideSearch.isVisible = false
                binding.noExistData.isVisible = false
                searchAdapter.addAllSearchData(viewState.list)
            }

            is SearchViewModel.SearchViewState.ResetSearchView -> {
                viewSearchBinding.editView.text.clear()
                viewSearchBinding.editView.requestFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }

            is SearchViewModel.SearchViewState.EmptySearchResult -> {
                binding.searchRv.isVisible = false
                binding.guideSearch.isVisible = false
                binding.noExistData.isVisible = true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        startSearchAdapter()
        settingSearchView()
    }

    private fun startSearchAdapter() {
        binding.searchRv.run {
            adapter = searchAdapter
            searchAdapter.clear()
            layoutManager = LinearLayoutManager(this@SearchActivity)
            searchAdapter.setSearchItemClickListener(this@SearchActivity)
        }
    }
}