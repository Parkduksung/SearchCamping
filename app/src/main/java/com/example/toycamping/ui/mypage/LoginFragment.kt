package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.LoginFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.LoginViewModel

class LoginFragment :
    BaseFragment<LoginFragmentBinding>(R.layout.login_fragment) {

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = loginViewModel

        initViewModel()
    }

    private fun initViewModel() {
        loginViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? LoginViewModel.LoginViewState)?.let { onChangedLoginViewState(viewState) }
        }
    }

    private fun onChangedLoginViewState(viewState: LoginViewModel.LoginViewState) {
        when (viewState) {

            is LoginViewModel.LoginViewState.EmptyUserId -> {
                showToast("아이디를 입력해 주세요.")
            }

            is LoginViewModel.LoginViewState.EmptyUserPass -> {
                showToast("패스워드를 입력해 주세요.")
            }

            is LoginViewModel.LoginViewState.LoginSuccess -> {
                showToast("로그인을 성공하였습니다.")
            }

            is LoginViewModel.LoginViewState.LoginFailure -> {
                showToast("로그인을 실패하였습니다.")
            }

            is LoginViewModel.LoginViewState.RouteResetPassword -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, ResetPassFragment()).commit()
            }

            is LoginViewModel.LoginViewState.RouteRegister -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, RegisterFragment()).commit()
            }
        }
    }
}