package com.example.p_scanner.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.*
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.database.ItemsDatabase
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.repository.Repository

class ProductViewModel(val context: Context):ViewModel() {

    val itemLiveData = MutableLiveData<Item>()
    val listItemsLiveData = MutableLiveData<List<Item>>()
    var itemAddedLiveData = MutableLiveData<Boolean>()

    var itemDAO:ItemDAO = ItemsDatabase.getDatabase(context).itemDAO()
    var repository = Repository(itemDAO)


    init {

        itemLiveData.observeForever(object :Observer<Item>{
            override fun onChanged(item: Item?) {
                if (!repository.itemIsExists(item!!.id))
                {
                    Handler(Looper.getMainLooper()).post { Toast.makeText(context ,"Add" ,Toast.LENGTH_SHORT).show() }
                    itemAddedLiveData.value = repository.insertItem(Item(item.id ,item.title ,item.description ,item.price ,item.type))
                }
                else
                {
                    Handler(Looper.getMainLooper()).post { Toast.makeText(context ,"That item already exists" ,Toast.LENGTH_SHORT).show() }
                }
            }
        })



        repository.getAllItems().observeForever(object :Observer<List<Item>>{
            override fun onChanged(listItems: List<Item>?) {
                listItemsLiveData.value = listItems!!
            }
        })
    }




}