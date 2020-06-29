package com.example.rfidtab.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.MainActivity
import com.example.rfidtab.R
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.ui.auth.AuthActivity


class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (AppPreferences.isLogined) {
            splashMain()
        } else {
            splashAuth()
        }
    }

    private fun splashMain() {
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH)
    }

    private fun splashAuth() {
        Handler().postDelayed({
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            this.finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}
