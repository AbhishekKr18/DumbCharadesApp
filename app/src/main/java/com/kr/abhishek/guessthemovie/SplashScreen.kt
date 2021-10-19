package com.kr.abhishek.guessthemovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kr.abhishek.guessthemovie.MainActivity as MainActivity1

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity1::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}