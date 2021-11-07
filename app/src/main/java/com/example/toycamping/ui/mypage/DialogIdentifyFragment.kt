package com.example.toycamping.ui.mypage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.toycamping.R
import com.example.toycamping.base.BaseDialogFragment
import com.example.toycamping.databinding.DialogIdentifyBinding

class DialogIdentifyFragment : BaseDialogFragment<DialogIdentifyBinding>(R.layout.dialog_identify)  {

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
            }

            yes.setOnClickListener {
                dismiss()
            }

        }
    }


    companion object {
        private const val KEY_TITLE = "TITLE"

        fun newInstance(title: String) =
            DialogIdentifyFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title
                )
            }
    }
}