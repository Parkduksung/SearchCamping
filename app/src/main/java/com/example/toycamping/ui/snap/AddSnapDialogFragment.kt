package com.example.toycamping.ui.snap

import android.app.Activity
import android.app.Dialog
import android.content.Intent
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
import com.example.toycamping.databinding.AddSnapDialogBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.viewmodel.AddSnapDialogViewModel

class AddSnapDialogFragment : BaseDialogFragment<AddSnapDialogBinding>(R.layout.add_snap_dialog) {

    private val addSnapDialogViewModel by viewModels<AddSnapDialogViewModel>()

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

        addSnapDialogViewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState: ViewState? ->
            (viewState as? AddSnapDialogViewModel.AddSnapDialogViewState)?.let {
                onChangedAddSnapDialogViewState(
                    viewState
                )
            }
        }

    }

    private fun onChangedAddSnapDialogViewState(viewState: AddSnapDialogViewModel.AddSnapDialogViewState) {

        when (viewState) {

            is AddSnapDialogViewModel.AddSnapDialogViewState.AddSnap -> {
                setFragmentResult(SUBMIT, bundleOf(ITEM to viewState.item, URI to viewState.uri))
                dismiss()
            }

            is AddSnapDialogViewModel.AddSnapDialogViewState.Error -> {
                showToast(message = viewState.message)
            }
        }

    }


    private fun initUi() {
        with(binding) {

            arguments?.run {
                setTitle(getString(KEY_TITLE, ""))
            }

            image.setOnClickListener {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
            }

            yes.setOnClickListener {
                with(binding) {
                    addSnapDialogViewModel.titleObservableField.set(etTitle.text.toString())
                    addSnapDialogViewModel.detailObservableField.set(etDetail.text.toString())
                }
                addSnapDialogViewModel.addSnap()
            }

            no.setOnClickListener {
                dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            //이미지 선택시
            if (resultCode == Activity.RESULT_OK) {
                //이미지뷰에 이미지 세팅
                addSnapDialogViewModel.imageUriObservableField.set(data?.data)
                binding.image.setImageURI(data?.data)
            }
        }
    }

    companion object {
        const val SUBMIT = "submit"
        const val ITEM = "item"
        const val URI = "uri"

        private const val KEY_TITLE = "TITLE"

        const val PICK_IMAGE_FROM_ALBUM = 0

        fun newInstance(title: String) =
            AddSnapDialogFragment().apply {
                arguments = bundleOf(
                    KEY_TITLE to title
                )
            }
    }
}