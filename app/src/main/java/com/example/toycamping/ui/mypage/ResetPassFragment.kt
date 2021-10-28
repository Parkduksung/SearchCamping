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
import com.example.toycamping.databinding.ResetPassFragmentBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.ResetPassViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPassFragment : BaseFragment<ResetPassFragmentBinding>(R.layout.reset_pass_fragment) {

    private val resetPassViewModel by viewModel<ResetPassViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
    }

    private fun initViewModel() {
        binding.viewModel = resetPassViewModel

        resetPassViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? ResetPassViewModel.ResetPassViewState)?.let {
                onChangedLoginViewState(viewState)
            }
        }
    }

    private fun onChangedLoginViewState(viewState: ResetPassViewModel.ResetPassViewState) {
        when (viewState) {

            is ResetPassViewModel.ResetPassViewState.ResetPassSuccess -> {
                showToast(
                    App.instance.context(),
                    message = """
                    비밀번호 초기화에 성공하였습니다.
                    메일에서 비밀번호를 변경하세요!
                """.trimIndent()
                )
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container_route, LoginFragment()).commit()
            }

            is ResetPassViewModel.ResetPassViewState.ResetPassFailure -> {
                showToast(message = "비밀번호 초기화에 실패하였습니다.")
            }

            is ResetPassViewModel.ResetPassViewState.EmptyResetId -> {
                showToast(message = "초기화할 아이디를 입력하세요.")
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