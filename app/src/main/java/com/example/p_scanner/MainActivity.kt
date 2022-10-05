package com.example.p_scanner

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.p_scanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    lateinit var scanningFragment: ScanningFragment
    lateinit var listProductsFragment: ListProductsFragment

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scanningFragment = ScanningFragment()
        listProductsFragment = ListProductsFragment()


        binding.buScanner.setOnClickListener {
            binding.buScanner.setBackgroundResource(R.drawable.background_bu_scanner)
            binding.buListProducts.setBackgroundColor(android.R.color.transparent)
            binding.buListProducts.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buScanner.setColorFilter(Color.parseColor("#5A6CF3"))
            setFragment(scanningFragment)
        }

        binding.buListProducts.setOnClickListener {
            binding.buListProducts.setBackgroundResource(R.drawable.background_bu_list_products)
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buListProducts.setColorFilter(Color.parseColor("#F08F5F"))
            setFragment(listProductsFragment)
        }
    }

    private fun setFragment(fragment: Fragment)
    {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fragment_container ,fragment)
        fr.commit()
    }
}