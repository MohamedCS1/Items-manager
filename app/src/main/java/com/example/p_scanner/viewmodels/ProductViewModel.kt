package com.example.p_scanner.viewmodels

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.repository.Repository

class ProductViewModel(owner: LifecycleOwner ,val context: Context):ViewModel() {

    val itemLiveData = MutableLiveData<Item>()
    val listItemsLiveData = MutableLiveData<List<Item>>()

    var itemDAO:ItemDAO = ItemsDatabase.getDatabase(context).itemDAO()
    var repository = Repository(itemDAO)


    init {

        itemLiveData.observeForever(object :Observer<Item>{
            override fun onChanged(item: Item?) {
                    try {
                        Log.d("CurrentItem" ,item.toString())
                        repository.insertItem(Item(item!!.id ,item.name ,item.description ,item.price ,item.type))
                        try {
                            (context as Activity).finish()
                        }catch (ex:Exception){}
                    }catch (ex: SQLiteException){
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context,"this product already exist" , Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        })



        itemDAO.getAllItems().observe(owner ,object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                listItemsLiveData.value = listItems!!
            }
        })
    }




}