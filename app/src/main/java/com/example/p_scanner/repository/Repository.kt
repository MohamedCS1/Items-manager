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

    fun getAllItems()
    {
        GlobalScope.launch {
            itemDao.getAllItems()
        }
    }

    fun itemIsExists(id:String):Boolean = runBlocking {
            itemDao.itemIsExists(id)
    }


}