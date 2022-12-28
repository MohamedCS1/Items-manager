package com.example.p_scanner.repository

import androidx.lifecycle.LiveData
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.pojo.Item
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class Repository(itemDAO: ItemDAO) {
    var itemDao:ItemDAO = itemDAO

    fun insertItem(item: Item)
    {
        GlobalScope.launch {
            itemDao.insertItem(item)
        }
    }

    fun getItemById(id:String):LiveData<Item> = runBlocking{
             itemDao.getItemById(id)
    }

    fun deleteItemById(id:String){
        GlobalScope.launch {
            itemDao.deleteById(id)
        }
    }

    fun getAllItems():LiveData<List<Item>> = runBlocking {
            itemDao.getAllItems()
    }

    fun itemIsExists(id:String):Boolean = runBlocking {
            itemDao.itemIsExists(id)
    }

    fun updateItemById(id:String ,newTitle:String ,newDescription:String ,newPrice:String)
    {
        GlobalScope.launch {
            itemDao.updateItemById(id ,newTitle ,newDescription ,newPrice)
        }
    }
}