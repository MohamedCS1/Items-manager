package com.example.p_scanner.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.p_scanner.pojo.Item
import com.example.p_scanner.pojo.ItemType

@Dao
interface ItemDAO {

    @Insert
    fun insertItem(item: Item)

    @Query("select * from ItemsTable")
    fun getAllItems():LiveData<List<Item>>

    @Query("select * from ItemsTable where id=:id")
    fun getItemById(id:String):LiveData<Item>

    @Query("select exists(select * from ItemsTable where id=:id)")
    fun itemIsExists(id:String) : Boolean

    @Query("delete from ItemsTable where id =:id")
    fun deleteById(id:String)

    @Query("update ItemsTable set title=:newTitle, description=:newDescription ,price=:newPrice ,type=:newType where id =:id")
    fun updateItemById(id:String ,newTitle:String ,newDescription:String ,newPrice:String ,newType:ItemType)
}
