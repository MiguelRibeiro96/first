package com.example.cookit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

import com.example.cookit.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private val SPLASHTIMEOUT = 3000L // 3 seconds
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Handler().postDelayed({
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }, SPLASHTIMEOUT)
    }
}








    
