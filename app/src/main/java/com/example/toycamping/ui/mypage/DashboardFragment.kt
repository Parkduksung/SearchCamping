package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.toycamping.App
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.DashboardFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.DashBoardViewModel

class DashboardFragment : BaseFragment<DashboardFragmentBinding>(R.layout.dashboard_fragment) {


    private val dashboardViewModel by viewModels<DashBoardViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = dashboardViewModel

        initViewModel()
    }

    private fun initViewModel() {
        dashboardViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? DashBoardViewModel.DashBoardViewState)?.let {
                onChangedLoginViewState(
                    viewState
                )
            }
        }
    }

    private fun onChangedLoginViewState(viewState: DashBoardViewModel.DashBoardViewState) {
        when (viewState) {

            is DashBoardViewModel.DashBoardViewState.LogoutSuccess -> {
                showToast(App.instance.context(),"로그아웃 성공.")
            }

            is DashBoardViewModel.DashBoardViewState.LogoutFailure -> {
                showToast(message = "로그아웃 실패.")
            }
        }
    }

}