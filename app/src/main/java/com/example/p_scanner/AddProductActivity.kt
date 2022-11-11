package com.example.p_scanner

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.p_scanner.DAO.ProductDAO
import com.example.p_scanner.Database.ProductsDatabase
import com.example.p_scanner.Pojo.Product
import com.example.p_scanner.ViewModels.ProductViewModel
import com.example.p_scanner.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddProductBinding
    lateinit var productViewModel: ProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productViewModel = ProductViewModel(this ,this)

        val productID = intent.extras?.getString("ProductID")?:""

        if (productID != "")
        {
            binding.tvId.setText(productID)
        }

        binding.buBack.setOnClickListener {
            onBackPressed()
        }

        binding.buCancel.setOnClickListener {
            finish()
        }

        binding.buAddItem.setOnClickListener {
            productViewModel.setProductLiveData.value = Product(binding.tvId.text.toString() ,binding.tvName.text.toString() ,binding.tvDescription.text.toString() ,binding.tvPrice.text.toString())
        }
    }
}