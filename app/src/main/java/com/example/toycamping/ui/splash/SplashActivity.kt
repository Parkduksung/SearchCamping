package com.example.toycamping.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.example.toycamping.R
import com.example.toycamping.base.BaseActivity
import com.example.toycamping.base.ViewState
import com.example.toycamping.databinding.ActivitySplashBinding
import com.example.toycamping.ext.showToast
import com.example.toycamping.ui.home.HomeActivity
import com.example.toycamping.viewmodel.SplashViewModel
import kotlin.system.exitProcess

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val splashViewModel by viewModels<SplashViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initViewModel()
    }

    private fun initViewModel() {
        lifecycle.addObserver(splashViewModel)
        splashViewModel.viewStateLiveData.observe(this) { viewState: ViewState? ->
            (viewState as? SplashViewModel.SplashViewState)?.let { onChangedViewState(viewState) }
        }
    }

    private fun onChangedViewState(viewState: SplashViewModel.SplashViewState) {
        when (viewState) {

            is SplashViewModel.SplashViewState.RouteMain -> {
                routeHomeActivity()
            }

            is SplashViewModel.SplashViewState.LoadData -> {
                showToast(
                    message = """
                    데이터 로딩중입니다.
                    잠시만 기다려 주세요.
                """.trimIndent()
                )
            }

            is SplashViewModel.SplashViewState.Error -> {
                showToast(
                    message = """
                        문제가 발생하였습니다.
                        앱을 다시 실행해 주세요.
                """.trimIndent()
                )
                exitProcess(0)
            }

        }
    }


    private fun routeHomeActivity() {
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable {
                val intent = Intent(this@SplashActivity, HomeActivity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
                startActivity(intent)
            },
            DELAY_ROUTE_TIME
        )
    }

    companion object {
        private const val DELAY_ROUTE_TIME = 2000L

    }
}

