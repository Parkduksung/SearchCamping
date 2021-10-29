package com.example.toycamping.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.toycamping.ext.showToast
import com.google.android.material.appbar.AppBarLayout

abstract class BaseActivity<B : ViewDataBinding>(@LayoutRes private val layoutId: Int) :
    AppCompatActivity(), ToolbarProvider {

    protected lateinit var binding: B

    private var toolbarHelper: ToolbarHelper? = null

    private var backWait: Long = INIT_TIME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(binding.root)
        initToolbarHelper()
    }


    override fun onBackPressed() {
        if (System.currentTimeMillis() - backWait >= LIMIT_TIME) {
            backWait = System.currentTimeMillis()
            showToast(message = "뒤로가기 버튼을 한번 더 누르면 종료됩니다.")
        } else {
            super.onBackPressed()
        }
    }

    private fun initToolbarHelper() {
        val appBarLayout =
            (binding.root as ViewGroup).children.find { it is AppBarLayout } as? AppBarLayout

        if (appBarLayout != null) {
            toolbarHelper = ToolbarHelper(this, appBarLayout).apply {
                setToolbarTitle(EMPTY_TOOLBAR_TITLE)
            }
        }
    }

    override fun setToolbarTitle(@StringRes titleResId: Int) {
        toolbarHelper?.setToolbarTitle(titleResId)
    }

    override fun setToolbarTitle(title: String?) {
        toolbarHelper?.setToolbarTitle(title)
    }

    override fun setNavigationIcon(@DrawableRes iconResId: Int) {
        toolbarHelper?.setNavigationIcon(iconResId)
    }

    override fun setCustomView(view: View) {
        toolbarHelper?.setCustomView(view)
    }

    override fun setCustomView(view: View, relativeHeight: Boolean) {
        toolbarHelper?.setCustomView(view, relativeHeight)
    }

    override fun getCustomView(): View? {
        return toolbarHelper?.getCustomView()
    }

    override fun setToolbarVisibility(isVisible: Boolean) {
        toolbarHelper?.setToolbarVisibility(isVisible)
    }

    companion object {
        private const val EMPTY_TOOLBAR_TITLE = ""
        private const val INIT_TIME = 0L
        private const val LIMIT_TIME = 2000

    }
}