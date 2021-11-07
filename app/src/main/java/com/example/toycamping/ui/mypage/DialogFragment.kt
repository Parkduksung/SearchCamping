package com.example.toycamping.ui.mypage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseDialogFragment
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.DialogMypageBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.DialogViewModel

class DialogFragment : BaseDialogFragment<DialogMypageBinding>(R.layout.dialog_mypage) {

    private val myPageDialogViewModel by viewModels<DialogViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initViewModel()
    }

    private fun initViewModel() {

        binding.viewModel = myPageDialogViewModel

        myPageDialogViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? DialogViewModel.MyPageDialogViewState)?.let {
                onChangedMyPageDialogViewState(
                    viewState
                )
            }
        }
    }

    private fun onChangedMyPageDialogViewState(viewState: DialogViewModel.MyPageDialogViewState) {

        when (viewState) {
            is DialogViewModel.MyPageDialogViewState.ClickOK -> {
                val getType = arguments?.getString(KEY_TYPE)
                setFragmentResult(SUBMIT, bundleOf(TYPE to getType))
                dismiss()
            }

            is DialogViewModel.MyPageDialogViewState.ClickNO -> {
                dismiss()
            }

            is DialogViewModel.MyPageDialogViewState.Error -> {
                showToast(message = viewState.message)
            }
        }
    }


    private fun initUi() {
        with(binding) {
            arguments?.run {
                title = getString(KEY_TITLE, "")
                detail = getString(KEY_DETAIL, "")
                buttonTitle = getString(KEY_BUTTON_TITLE, "")
            }
        }
    }

    companion object {
        const val SUBMIT = "submit"
        const val TYPE = "type"

        private const val KEY_TITLE = "TITLE"
        private const val KEY_DETAIL = "DETAIL"
        private const val KEY_BUTTON_TITLE = "BUTTON_TITLE"
        private const val KEY_TYPE = "TYPE"

        fun newInstance(
            title: String,
            detail: String,
            buttonTitle: String,
            type: String
        ) =
            DialogFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_DETAIL to detail,
                    KEY_BUTTON_TITLE to buttonTitle,
                    KEY_TYPE to type,
                )
            }
    }
}