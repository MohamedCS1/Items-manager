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

                if (!repository.itemIsExists(item!!.id))
                {
                    Toast.makeText(context ,"Add" ,Toast.LENGTH_SHORT).show()
                    repository.insertItem(Item(item.id ,item.name ,item.description ,item.price ,item.type))
                    (context as Activity).finish()
                }
                else
                {
                    (context as Activity).runOnUiThread {
                        Toast.makeText(context ,"That item already exists" ,Toast.LENGTH_SHORT).show()
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