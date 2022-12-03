package com.example.p_scanner.ViewModels

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.BarCodeScanner.BarCodeAnalyzer
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Interfaces.BarCodeInterfaces
import com.example.p_scanner.Pojo.Item
import com.google.mlkit.vision.barcode.common.Barcode

class ProductViewModel(owner: LifecycleOwner ,val context: Context):ViewModel() {
    val productBarCodeDetectLiveData = MutableLiveData<String>()
    val setItemLiveData = MutableLiveData<Item>()
    val getListItemLiveData = MutableLiveData<List<Item>>()

    val db = ItemsDatabase.getDatabase(context)
    val productDAO = db.productDAO()
    init {


        setItemLiveData.observe(owner,object :Observer<Item>{
            override fun onChanged(item: Item?) {
                db.queryExecutor.execute {
                    try {
                        productDAO.insertProduct(Item(item!!.id ,item.name ,item.description ,item.price ,item.type))
                        (context as Activity).finish()
                    }catch (ex: SQLiteException){
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context,"this product already exist" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })

        productDAO.getProduct().observe(owner ,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                getListItemLiveData.value = listItems!!
            }
        })

    }

}