package com.example.toycamping.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
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
                onChangedRegisterViewState(viewState)
            }
        }
    }

    private fun onChangedRegisterViewState(viewState: RegisterViewModel.RegisterViewState) {
        when (viewState) {

            is RegisterViewModel.RegisterViewState.RegisterSuccess -> {
                showToast(App.instance.context(), message = "회원가입에 성공하였습니다.")
            }

            is RegisterViewModel.RegisterViewState.RegisterFailure -> {
                showToast(message = "회원가입에 실패하였습니다.")
            }

            is RegisterViewModel.RegisterViewState.EmptyUserId -> {
                showToast(message = "회원가입 아이디를 입력하세요.")
            }

            is RegisterViewModel.RegisterViewState.EmptyUserPass -> {
                showToast(message = "회원가입 패스워드를 입력하세요.")
            }

            is RegisterViewModel.RegisterViewState.EmptyUserNickname -> {
                showToast(message = "회원가입 닉네임을 입력하세요.")
            }

            is RegisterViewModel.RegisterViewState.ShowProgress -> {
                binding.progressbar.isVisible = true
            }

            is RegisterViewModel.RegisterViewState.HideProgress -> {
                binding.progressbar.isVisible = false
            }

            is RegisterViewModel.RegisterViewState.RouteLogin -> {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, LoginFragment()).commit()
            }
        }
    }

}