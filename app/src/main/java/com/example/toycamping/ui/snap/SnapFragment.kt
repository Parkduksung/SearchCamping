package com.example.toycamping.ui.snap

import android.net.Uri
import android.os.Bundle
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
        snapViewModel.checkLoginState()
    }

    private fun initUi() {
        binding.rvSnap.run {
            adapter = snapAdapter
        }
        snapAdapter.setOnLongClickListener {
            binding.progressbar.isVisible = true
            homeViewModel.deleteSnapItem(it)
        }
        snapViewModel.getAllSnapList()
    }


    private fun initViewModel() {

        binding.viewModel = snapViewModel

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
                snapViewModel.getAllSnapList()
            }

            is HomeViewModel.HomeViewState.AddSnapItem -> {
                showToast(message = "스냅이 추가되었습니다.")
                snapAdapter.add(item = viewState.item)
                binding.rvSnap.isVisible = true
                binding.tvEmptySnap.isVisible = false
                binding.progressbar.isVisible = false
            }

            is HomeViewModel.HomeViewState.DeleteSnapItem -> {
                showToast(message = "스냅이 삭제되었습니다.")
                snapAdapter.delete(item = viewState.item)
                binding.progressbar.isVisible = false
                if (snapAdapter.itemCount == 0) {
                    binding.rvSnap.isVisible = false
                    binding.tvEmptySnap.isVisible = true
                }

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
                binding.progressbar.isVisible = true
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
                binding.rvSnap.isVisible = true
                binding.tvEmptySnap.isVisible = false
            }

            is SnapViewModel.SnapViewState.ShowLoginView -> {
                homeViewModel.startLoginView()
            }

            is SnapViewModel.SnapViewState.EmptySnapList -> {
                binding.rvSnap.isVisible = false
                binding.tvEmptySnap.isVisible = true
            }

            is SnapViewModel.SnapViewState.ShowProgress -> {
                binding.progressbar.isVisible = true
            }

            is SnapViewModel.SnapViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }
        }
    }
}