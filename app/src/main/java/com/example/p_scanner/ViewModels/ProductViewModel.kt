package com.example.p_scanner.ViewModels

import android.app.Activity
import android.app.Application
import android.content.Context
import android.database.sqlite.SQLiteException
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.DAO.ItemDAO
import com.example.p_scanner.Database.ItemsDatabase
import com.example.p_scanner.Pojo.Item

class ProductViewModel(val owner: LifecycleOwner ,val context: Context):ViewModel() {
    val productBarCodeDetectLiveData = MutableLiveData<String>()
    val itemLiveData = MutableLiveData<Item>()
    val listItemsLiveData = MutableLiveData<List<Item>>()
    var itemDAO:ItemDAO
    val db = ItemsDatabase.getDatabase(context)
    init {
        itemDAO = db.itemDAO()
    }

    fun startItemLiveDataObservation()
    {
        itemLiveData.observe(owner,object :Observer<Item>{
            override fun onChanged(item: Item?) {
                db.queryExecutor.execute {
                    try {
                        itemDAO.insertProduct(Item(item!!.id ,item.name ,item.description ,item.price ,item.type))
                        (context as Activity).finish()
                    }catch (ex: SQLiteException){
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context,"this product already exist" , Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }

    fun passAllItemsToListItemLiveData()
    {
        itemDAO.getAllItems().observe(owner ,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                setValueToListItems(listItems!!)
            }
        })

    }

    fun setValueToListItems(listItems:List<Item>)
    {
        if (listItems.isNotEmpty())
        {
            listItemsLiveData.value = listItems
        }

    }

}