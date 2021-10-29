package com.example.toycamping.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.SnapFragmentBinding
import com.example.toycamping.viewmodel.HomeViewModel

class SnapFragment : BaseFragment<SnapFragmentBinding>(R.layout.snap_fragment) {

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        setToolbarVisibility(false)
    }

    private fun initViewModel() {

        homeViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? HomeViewModel.HomeViewState)?.let {
                onChangedHomeViewState(viewState)
            }
        }

    }

    private fun onChangedHomeViewState(viewState: HomeViewModel.HomeViewState) {
        when (viewState) {

            is HomeViewModel.HomeViewState.NotLoginState -> {
                binding.containerNotLoginSnap.isVisible = true
            }

            is HomeViewModel.HomeViewState.LoginState -> {
                binding.containerNotLoginSnap.isVisible = false
            }
        }
    }
}