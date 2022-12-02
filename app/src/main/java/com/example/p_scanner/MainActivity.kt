package com.example.p_scanner

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.p_scanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    lateinit var scanningFragment: ScanningFragment
    lateinit var listItemsFragment: ListItemsFragment
    lateinit var searchFragment: SearchFragment

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkCameraPermission()

        scanningFragment = ScanningFragment()
        listItemsFragment = ListItemsFragment()
        searchFragment = SearchFragment()

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
            binding.buListProducts.setBackgroundResource(R.drawable.background_radius_second_color)
            binding.buListProducts.setColorFilter(Color.parseColor("#F08F5F"))
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buSearch.setBackgroundColor(android.R.color.transparent)
            binding.buSearch.setColorFilter(Color.parseColor("#BBBBBB"))
            setFragment(listItemsFragment)
        }

        binding.buSearch.setOnClickListener {
            binding.buSearch.setBackgroundResource(R.drawable.background_bu_search)
            binding.buSearch.setColorFilter(Color.parseColor("#2DC0FF"))
            binding.buScanner.setBackgroundColor(android.R.color.transparent)
            binding.buScanner.setColorFilter(Color.parseColor("#BBBBBB"))
            binding.buListProducts.setBackgroundColor(android.R.color.transparent)
            binding.buListProducts.setColorFilter(Color.parseColor("#BBBBBB"))
            setFragment(searchFragment)
        }

    }

    override fun onStart() {
        checkCameraPermission()
        super.onStart()
    }

    private fun setFragment(fragment: Fragment)
    {
        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.fragment_container ,fragment)
        fr.commit()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0)
        {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                setFragment(scanningFragment)
            }
            else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                intentToSettings()
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, arrayOf(android.Manifest.permission.CAMERA).toString()) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0)
        }
    }

    fun intentToSettings()
    {

        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}