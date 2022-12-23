package com.example.p_scanner.ViewModels

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.DAO.ItemDAO
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Pojo.Item

class ProductViewModel(owner: LifecycleOwner ,val context: Context):ViewModel() {

    val productBarCodeDetectLiveData = MutableLiveData<String>()
    val itemLiveData = MutableLiveData<Item>()
    val listItemsLiveData = MutableLiveData<List<Item>>()

    val db = ItemsDatabase.getDatabase(context)

    var itemDAO:ItemDAO = db.itemDAO()
    init {

        itemLiveData.observeForever(object :Observer<Item>{
            override fun onChanged(item: Item?) {
                db.queryExecutor.execute {
                    try {
//                        Log.d("CurrentItem" ,item.toString())
                        itemDAO.insertItem(Item(item!!.id ,item.name ,item.description ,item.price ,item.type))
                        try {
                            (context as Activity).finish()
                        }catch (ex:Exception){}
                    }catch (ex: SQLiteException){
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context,"this product already exist" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })



//        itemDAO.getAllItems().observe(owner ,object :Observer<List<Item>>{
//            override fun onChanged(listItems: List<Item>?) {
//                listItemsLiveData.value = listItems!!
//            }
//        })
    }




}