package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.MypageFragmentBinding
import com.example.toycamping.viewmodel.HomeViewModel

class MyPageFragment : BaseFragment<MypageFragmentBinding>(R.layout.mypage_fragment) {


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
                childFragmentManager.beginTransaction()
                    .replace(R.id.container_route, LoginFragment())
                    .commit()
            }

            is HomeViewModel.HomeViewState.LoginState -> {
                childFragmentManager.beginTransaction()
                    .replace(R.id.container_route, DashboardFragment())
                    .commit()
            }
        }
    }
}