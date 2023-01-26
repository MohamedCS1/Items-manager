package com.example.p_scanner.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.p_scanner.databinding.ActivitySplashScreenBinding
import com.example.p_scanner.sharedPreferences.SharedPreference
import com.example.p_scanner.ui.main.MainActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashScreenBinding

    val sharedPreference: SharedPreference by lazy{
        SharedPreference(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPreference.splashScreenIsSeen()){
            finish()
            startActivity(Intent(this , MainActivity::class.java))
        }
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buStart.setOnClickListener {
            sharedPreference.splashScreenIsSeenNow()
            startActivity(Intent(this , MainActivity::class.java))
            finish()
        }
    }

}