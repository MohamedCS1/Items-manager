package com.example.p_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.p_scanner.databinding.ActivityMainBinding
import com.example.p_scanner.databinding.ActivitySplashScreenBinding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.itemIconTintList = null

        binding.bottomNavigationView.setOnItemSelectedListener(object :NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                return true
            }
        })
        binding.bottomNavigationView.setOnItemReselectedListener(object :NavigationBarView.OnItemReselectedListener{
            override fun onNavigationItemReselected(item: MenuItem) {
            }
        })
    }
}