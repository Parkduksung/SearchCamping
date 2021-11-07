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
import com.example.toycamping.databinding.DialogAddQuestionBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.AddQuestionViewModel

class AddQuestionDialogFragment :
    BaseDialogFragment<DialogAddQuestionBinding>(R.layout.dialog_add_question) {


    private val addQuestionViewModel by viewModels<AddQuestionViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        initUi()
    }

    private fun initViewModel() {

        addQuestionViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? AddQuestionViewModel.AddQuestionViewState)?.let {
                onChangedAddQuestionViewState(
                    viewState
                )
            }
        }

    }

    private fun onChangedAddQuestionViewState(viewState: AddQuestionViewModel.AddQuestionViewState) {

        when (viewState) {

            is AddQuestionViewModel.AddQuestionViewState.AddQuestion -> {
                setFragmentResult(SUBMIT, bundleOf(ITEM to viewState.item))
                dismiss()
            }

            is AddQuestionViewModel.AddQuestionViewState.Error -> {
                showToast(message = viewState.message)
            }
        }
    }


    private fun initUi() {
        with(binding) {

            arguments?.run {
                setTitle(getString(KEY_TITLE, ""))
            }

            yes.setOnClickListener {
                addQuestionViewModel.addQuestion(binding.etDetail.text.toString())
            }

            no.setOnClickListener {
                dismiss()
            }
        }
    }


    companion object {
        const val SUBMIT = "submit"
        const val ITEM = "item"

        private const val KEY_TITLE = "TITLE"

        fun newInstance(title: String) =
            AddQuestionDialogFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title
                )
            }
    }
}