package com.example.p_scanner.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.p_scanner.databinding.ActivitySplashScreenBinding
import com.example.p_scanner.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buStart.setOnClickListener {
            startActivity(Intent(this , MainActivity::class.java))

        }
    }

}