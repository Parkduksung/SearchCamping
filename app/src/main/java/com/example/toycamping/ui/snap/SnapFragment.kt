package com.example.toycamping.ui.snap

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.SnapItem
import com.example.toycamping.databinding.SnapFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.ui.adapter.SnapAdapter
import com.example.toycamping.viewmodel.HomeViewModel
import com.example.toycamping.viewmodel.SnapViewModel

class SnapFragment : BaseFragment<SnapFragmentBinding>(R.layout.snap_fragment) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    private val snapViewModel by viewModels<SnapViewModel>()

    private val snapAdapter by lazy { SnapAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModel()

    }

    override fun onResume() {
        super.onResume()
        setToolbarVisibility(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_snap, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_snap -> {
                snapViewModel.showAddSnapDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        binding.rvSnap.run {
            adapter = snapAdapter
        }
        snapAdapter.setOnLongClickListener {
            showToast(message = "롱클릭.")
            homeViewModel.deleteSnapItem(it)
        }
        snapViewModel.getAllSnapList()
    }


    private fun initViewModel() {

        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let {
                onChangedHomeViewState(viewState)
            }
        }

        snapViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? SnapViewModel.SnapViewState)?.let {
                onChangedSnapViewState(viewState)
            }
        }
    }

    private fun onChangedHomeViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {

            is HomeViewModel.HomeViewState.NotLoginState -> {
                binding.containerNotLoginSnap.isVisible = true
                binding.rvSnap.isVisible = false
                snapAdapter.clear()
            }

            is HomeViewModel.HomeViewState.LoginState -> {
                binding.containerNotLoginSnap.isVisible = false
                binding.rvSnap.isVisible = true
            }

            is HomeViewModel.HomeViewState.AddSnapItem -> {
                snapAdapter.add(item = viewState.item)
            }

            is HomeViewModel.HomeViewState.DeleteSnapItem -> {
                snapAdapter.delete(item = viewState.item)
            }
        }
    }

    private fun startAddSnapDialog() {
        val dialog = AddSnapDialogFragment.newInstance(title = "스냅 추가")
        dialog.show(parentFragmentManager, dialog::class.simpleName)

        parentFragmentManager.setFragmentResultListener(
            AddSnapDialogFragment.SUBMIT,
            this
        ) { _: String, bundle: Bundle ->

            val getItem = bundle.getParcelable<SnapItem>(AddSnapDialogFragment.ITEM)
            val getUri = bundle.getParcelable<Uri>(AddSnapDialogFragment.URI)

            if (getItem != null && getUri != null) {
                homeViewModel.addSnapItem(getItem, getUri)
            } else {
                showToast(message = "스냅 추가 실패.")
            }
        }
    }

    private fun onChangedSnapViewState(viewState: SnapViewModel.SnapViewState) {
        when (viewState) {

            is SnapViewModel.SnapViewState.AddSnapDialog -> {
                startAddSnapDialog()
            }

            is SnapViewModel.SnapViewState.Error -> {
                showToast(message = viewState.message)
            }

            is SnapViewModel.SnapViewState.SnapList -> {
                snapAdapter.addAll(viewState.list)
            }
        }
    }
}