package com.example.p_scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.p_scanner.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productID = intent.extras?.getString("ProductID")?:""

        if (productID != "")
        {
            binding.tvId.setText(productID)
        }

        binding.buBack.setOnClickListener {
            onBackPressed()
        }
    }
}