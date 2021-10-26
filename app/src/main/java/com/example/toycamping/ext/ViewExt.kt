package com.example.toycamping.ext

import android.content.Context
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
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