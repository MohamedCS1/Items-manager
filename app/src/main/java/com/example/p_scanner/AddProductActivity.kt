package com.example.p_scanner

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.p_scanner.DAO.ProductDAO
import com.example.p_scanner.Database.ProductsDatabase
import com.example.p_scanner.Pojo.Product
import com.example.p_scanner.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = ProductsDatabase.getDatabase(this)
        val productDAO = db.productDAO()

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
            db.queryExecutor.execute {
                try {

                    productDAO.insertProduct(Product(binding.tvId.text.toString() ,binding.tvName.text.toString() ,binding.tvDescription.text.toString() ,binding.tvPrice.text.toString()))
                    finish()
                }catch (ex:SQLiteException){
                    runOnUiThread {
                        Toast.makeText(this ,"this product already exist" ,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}