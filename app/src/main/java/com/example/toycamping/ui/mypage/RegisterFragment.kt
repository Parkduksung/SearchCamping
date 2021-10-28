package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.example.toycamping.App
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.RegisterFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : BaseFragment<RegisterFragmentBinding>(R.layout.register_fragment) {

    private val registerViewModel by viewModel<RegisterViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

    }


    private fun initViewModel() {
        binding.viewModel = registerViewModel

        registerViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? RegisterViewModel.RegisterViewState)?.let {
                onChangedLoginViewState(viewState)
            }
        }
    }

    private fun onChangedLoginViewState(viewState: RegisterViewModel.RegisterViewState) {
        when (viewState) {

            is RegisterViewModel.RegisterViewState.RegisterSuccess -> {
                showToast(App.instance.context(),message = "회원가입에 성공하였습니다.")
            }

            is RegisterViewModel.RegisterViewState.RegisterFailure -> {
                showToast(message = "회원가입에 실패하였습니다.")
            }

            is RegisterViewModel.RegisterViewState.EmptyUserId -> {

            }

            is RegisterViewModel.RegisterViewState.EmptyUserPass -> {

            }

            is RegisterViewModel.RegisterViewState.RouteMyPage -> {

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_back, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        setToolbarVisibility(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, LoginFragment()).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}