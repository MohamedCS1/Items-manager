package com.example.p_scanner.repository

import androidx.lifecycle.LiveData
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class Repository(itemDAO: ItemDAO) {
    var itemDao:ItemDAO = itemDAO

    fun insertItem(item: Item):Boolean = runBlocking{
        var itemInserted = false
            if (item.title.isNotBlank() && item.description.isNotBlank() && item.price.isNotBlank())
            {
                itemDao.insertItem(item)
                itemInserted = true
            }
        return@runBlocking itemInserted
    }

    fun getItemById(id:String):LiveData<Item> = runBlocking{
             itemDao.getItemById(id)
    }

    fun deleteItemById(id:String){
        GlobalScope.launch {
            itemDao.deleteItemById(id)
        }
    }

    fun getAllItems():LiveData<List<Item>> = runBlocking {
            itemDao.getAllItems()
    }

    fun itemIsExists(id:String):Boolean = runBlocking {
            itemDao.itemIsExists(id)
    }

    fun updateItemById(id:String ,newTitle:String ,newDescription:String ,newPrice:String ,newType:ItemType)
    {
        GlobalScope.launch {
            itemDao.updateItemById(id ,newTitle ,newDescription ,newPrice ,newType)
        }
    }

    fun clearDatabase()
    {
        GlobalScope.launch {
            itemDao.clearDatabase()
        }
    }
}