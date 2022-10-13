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
    lateinit var searchFragment: SearchFragment

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scanningFragment = ScanningFragment()
        listProductsFragment = ListProductsFragment()
        searchFragment = SearchFragment()

        setFragment(scanningFragment)
        binding.buScanner.setOnClickListener {
            binding.buScanner.setBackgroundResource(R.drawable.background_bu_scanner)
            binding.buScanner.setColorFilter(Color.parseColor("#5A6CF3"))
            binding.buListProducts.setBackgroundColor(android.R.color.transparent)
            binding.buListProducts.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buSearch.setBackgroundColor(android.R.color.transparent)
            binding.buSearch.setColorFilter(Color.parseColor("#BBBBBB"))
            setFragment(scanningFragment)
        }

        binding.buListProducts.setOnClickListener {
            binding.buListProducts.setBackgroundResource(R.drawable.background_bu_list_products)
            binding.buListProducts.setColorFilter(Color.parseColor("#F08F5F"))
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buSearch.setBackgroundColor(android.R.color.transparent)
            binding.buSearch.setColorFilter(Color.parseColor("#BBBBBB"))
            setFragment(listProductsFragment)
        }

        binding.buSearch.setOnClickListener {
            binding.buSearch.setBackgroundResource(R.drawable.background_bu_list_products)
            binding.buSearch.setColorFilter(Color.parseColor("#2DC0FF"))
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buListProducts.setBackgroundColor(android.R.color.transparent)
            binding.buListProducts.setColorFilter(Color.parseColor("#BBBBBB"))
            setFragment(searchFragment)
        }

    }

    private fun setFragment(fragment: Fragment)
    {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fragment_container ,fragment)
        fr.commit()
    }
}