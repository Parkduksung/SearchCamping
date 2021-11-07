package com.example.toycamping.ext

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import com.example.toycamping.R
import com.example.toycamping.ui.mypage.DialogFragment


fun ConstraintLayout.showPOIInfoContainer(context: Context) {
    bringToFront()
    isVisible = true
    startAnimation(
        AnimationUtils.loadAnimation(
            context,
            R.anim.slide_up
        )
    )
}

fun ConstraintLayout.hidePOIInfoContainer(context: Context) {
    if (isVisible) {
        isVisible = false
        startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.slide_down
            )
        )
    }
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showDialog(
    title: String,
    detail: String,
    titleButton: String,
    type: String,
    listener: FragmentResultListener
) {
    val dialog = DialogFragment.newInstance(
        title = title,
        detail = detail,
        buttonTitle = titleButton,
        type = type
    )
    dialog.show(parentFragmentManager, dialog::class.simpleName)
    parentFragmentManager.setFragmentResultListener(
        DialogFragment.SUBMIT,
        this,
        listener
    )
}

fun AppCompatActivity.showDialog(
    title: String,
    detail: String,
    titleButton: String,
    type: String,
    listener: FragmentResultListener
) {
    val dialog = DialogFragment.newInstance(
        title = title,
        detail = detail,
        buttonTitle = titleButton,
        type = type
    )
    dialog.show(supportFragmentManager, dialog::class.simpleName)
    supportFragmentManager.setFragmentResultListener(
        DialogFragment.SUBMIT,
        this,
        listener
    )
}


fun AppCompatActivity.showToast(context: Context = this, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
