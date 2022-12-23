package com.example.p_scanner.repository

import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.pojo.Item
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class Repository(itemDAO: ItemDAO) {
    var itemDao:ItemDAO = itemDAO

    fun insertItem(item: Item)
    {
        GlobalScope.launch{
            itemDao.insertItem(item)
        }
    }

    fun getItemById(id:String)
    {
        itemDao.getItemById(id)
    }

    fun getAllItems()
    {
        itemDao.getAllItems()
    }

}