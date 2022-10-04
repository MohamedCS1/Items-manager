package com.example.p_scanner

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.p_scanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buScanner.setOnClickListener {
            binding.buScanner.setBackgroundResource(R.drawable.background_bu_scanner)
            binding.buListProducts.setBackgroundColor(android.R.color.transparent)
            binding.buListProducts.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buScanner.setColorFilter(Color.parseColor("#5A6CF3"))
        }

        binding.buListProducts.setOnClickListener {
            binding.buListProducts.setBackgroundResource(R.drawable.background_bu_list_products)
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buListProducts.setColorFilter(Color.parseColor("#F08F5F"))
        }
    }
}