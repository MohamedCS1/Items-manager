package com.example.p_scanner.repository

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

    fun getItemById(id:String)
    {
        GlobalScope.launch {
            itemDao.getItemById(id)
        }
    }

    fun getAllItems()
    {
        GlobalScope.launch {
            itemDao.getAllItems()
        }
    }

    fun itemIsExists(id:String):Boolean
    {
        var isExists:Boolean? = null
        runBlocking {
            isExists = itemDao.itemIsExists(id)
        }
        return isExists!!
    }


}