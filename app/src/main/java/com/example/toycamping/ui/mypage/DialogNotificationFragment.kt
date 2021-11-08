package com.example.toycamping.ui.mypage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.toycamping.R
import com.example.toycamping.base.BaseDialogFragment
import com.example.toycamping.databinding.DialogNotificationBinding

class DialogNotificationFragment :
    BaseDialogFragment<DialogNotificationBinding>(R.layout.dialog_notification) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
    }


    private fun initUi() {
        with(binding) {

            arguments?.run {
                setTitle(getString(KEY_TITLE, ""))
                setDetail(getString(KEY_DETAIL, ""))
                setDate(getString(KEY_DATE, ""))
            }

            yes.setOnClickListener {
                dismiss()
            }

        }
    }


    companion object {
        private const val KEY_TITLE = "TITLE"
        private const val KEY_DETAIL = "DETAIL"
        private const val KEY_DATE = "DATE"

        fun newInstance(title: String, detail: String, date: String) =
            DialogNotificationFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title,
                    KEY_DETAIL to detail,
                    KEY_DATE to date
                )
            }
    }
}