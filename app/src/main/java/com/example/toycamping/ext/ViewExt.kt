package com.example.toycamping.ext

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.toycamping.R


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

fun Fragment.showToast(message : String) {
    Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
}