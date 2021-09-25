package com.example.toycamping.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.toycamping.R
import com.example.toycamping.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        routeHomeActivity()
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
        private const val DELAY_ROUTE_TIME = 1500L

    }
}

