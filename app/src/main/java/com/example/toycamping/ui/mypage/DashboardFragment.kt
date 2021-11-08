package com.example.toycamping.ui.mypage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.toycamping.App
import com.example.toycamping.R
import com.example.toycamping.base.BaseFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.data.model.NotificationItem
import com.example.toycamping.data.model.QuestionItem
import com.example.toycamping.databinding.DashboardFragmentBinding
import com.example.toycamping.ext.convertDate
import com.example.toycamping.ext.showDialog
import com.example.toycamping.ext.showToast
import com.example.toycamping.ui.adapter.NotificationAdapter
import com.example.toycamping.viewmodel.DashBoardViewModel

class DashboardFragment : BaseFragment<DashboardFragmentBinding>(R.layout.dashboard_fragment) {


    private val dashboardViewModel by viewModels<DashBoardViewModel>()

    private val notificationAdapter by lazy { NotificationAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = dashboardViewModel

        initUi()
        initViewModel()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                dashboardViewModel.showDashboard()
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun initUi() {
        dashboardViewModel.getUserInfo()

        binding.rvNotification.run {
            adapter = notificationAdapter
        }

        notificationAdapter.setOnItemClickListener {
            startNotificationDialog(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onChangedLoginViewState(viewState: DashBoardViewModel.DashBoardViewState) {
        when (viewState) {

            is DashBoardViewModel.DashBoardViewState.LogoutSuccess -> {
                showToast(App.instance.context(), "로그아웃 성공.")
            }

            is DashBoardViewModel.DashBoardViewState.LogoutFailure -> {
                showToast(message = "로그아웃 실패.")
            }

            is DashBoardViewModel.DashBoardViewState.WithdrawSuccess -> {
                showToast(App.instance.context(), "회원탈퇴 성공.")
            }

            is DashBoardViewModel.DashBoardViewState.WithdrawFailure -> {
                showToast(message = "회원탈퇴 실패.")
            }

            is DashBoardViewModel.DashBoardViewState.EmptyNotificationItem -> {
                showToast(message = "공지사항 없음.")
            }

            is DashBoardViewModel.DashBoardViewState.ErrorGetNotificationItem -> {
                showToast(message = "공지사항 가져오기를 실패하였습니다.")
            }

            is DashBoardViewModel.DashBoardViewState.ShowLogoutDialog -> {
                showDialog(
                    title = "로그아웃 하시겠어요?",
                    detail = "로그아웃시 즐겨찾기, 스냅 기능을 사용할 수 없어요!",
                    titleButton = "로그아웃",
                    type = "logout"
                ) { _, result ->
                    val getResultType = result.getString(DialogFragment.TYPE)
                    dashboardViewModel.checkType(getResultType)
                }
            }

            is DashBoardViewModel.DashBoardViewState.ShowWithdrawDialog -> {
                showDialog(
                    title = "회원탈퇴 하시겠어요?",
                    detail = "회원탈퇴시 회원님의 모든 정보가 사라집니다",
                    titleButton = "회원탈퇴",
                    type = "withdraw"
                ) { _, result ->
                    val getResultType = result.getString(DialogFragment.TYPE)
                    dashboardViewModel.checkType(getResultType)
                }
            }

            is DashBoardViewModel.DashBoardViewState.ShowNotification -> {
                viewNotification(isShow = true)
                notificationAdapter.getAll(viewState.list)
            }

            is DashBoardViewModel.DashBoardViewState.ShowDashboard -> {
                viewNotification(isShow = false)
                notificationAdapter.clear()
            }


            is DashBoardViewModel.DashBoardViewState.ShowQuestion -> {
                startAddQuestionDialog()
            }

            is DashBoardViewModel.DashBoardViewState.ShowIdentify -> {
                startIdentifyDialog()
            }

            is DashBoardViewModel.DashBoardViewState.AddQuestionFailure -> {
                showToast(message = "문의사항 등록이 실패하였습니다.")
            }

            is DashBoardViewModel.DashBoardViewState.AddQuestionSuccess -> {
                showToast(message = "문의사항이 등록되었습니다.")
            }

            is DashBoardViewModel.DashBoardViewState.GetUserInfo -> {
                with(binding) {
                    tvLoginId.text = viewState.id
                    tvLoginNickname.text = "${viewState.nickname}님 환영합니다."
                }
            }
        }
    }

    private fun startAddQuestionDialog() {
        val dialog = AddQuestionDialogFragment.newInstance(title = "문의사항")
        dialog.show(parentFragmentManager, dialog::class.simpleName)

        parentFragmentManager.setFragmentResultListener(
            AddQuestionDialogFragment.SUBMIT,
            this
        ) { _: String, bundle: Bundle ->
            val getQuestionItem = bundle.getParcelable<QuestionItem>(AddQuestionDialogFragment.ITEM)
            dashboardViewModel.addQuestion(getQuestionItem)
        }
    }

    private fun startNotificationDialog(item: NotificationItem) {
        val dialog = DialogNotificationFragment.newInstance(
            title = item.title.orEmpty(),
            detail = item.detail.orEmpty(),
            date = item.date?.convertDate().orEmpty()
        )
        dialog.show(parentFragmentManager, dialog::class.simpleName)
    }


    private fun startIdentifyDialog() {
        val dialog = DialogIdentifyFragment.newInstance(title = "개인정보처리방침")
        dialog.show(parentFragmentManager, dialog::class.simpleName)
    }

    private fun viewNotification(isShow: Boolean) {
        binding.containerDashboard.isVisible = !isShow
        binding.containerNotification.isVisible = isShow
        setToolbarVisibility(isShow)
        if (isShow) {
            setNavigationIcon(R.drawable.ic_back)
        }
    }
}