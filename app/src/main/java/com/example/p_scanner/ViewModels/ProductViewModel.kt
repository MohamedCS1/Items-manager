package com.example.p_scanner.ViewModels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.AddProductActivity
import com.example.p_scanner.BarCodeScanner.BarCodeAnalyzer
import com.example.p_scanner.Database.ProductsDatabase
import com.example.p_scanner.Interfaces.BarCodeInterfaces
import com.example.p_scanner.Pojo.Product
import com.google.mlkit.vision.barcode.common.Barcode

class ProductViewModel(owner: LifecycleOwner ,context: Context):ViewModel() {
    val productBarCodeDetectLiveData = MutableLiveData<String>()
    val setProductLiveData = MutableLiveData<Product>()
    val getListProductLiveData = MutableLiveData<List<Product>>()

    val db = ProductsDatabase.getDatabase(context)
    val productDAO = db.productDAO()

    init {
        val barCodeAnalyzer = BarCodeAnalyzer(context)
        barCodeAnalyzer.onBarCodeDetection(object : BarCodeInterfaces {
            override fun onBarCodeDetection(barcode: Barcode) {
                productBarCodeDetectLiveData.value = barcode.rawValue.toString()
            }
        })

        setProductLiveData.observe(owner,object :Observer<Product>{
            override fun onChanged(product: Product?) {
                db.queryExecutor.execute {
                    try {
                        productDAO.insertProduct(Product(product!!.id ,product.name ,product.description ,product.price))
                        (context as Activity).finish()
                    }catch (ex: SQLiteException){
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context,"this product already exist" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        productDAO.getProduct().observe(owner ,object :Observer<List<Product>>{
            override fun onChanged(ListProducts: List<Product>?) {
                getListProductLiveData.value = ListProducts!!
            }
        })

    }


}