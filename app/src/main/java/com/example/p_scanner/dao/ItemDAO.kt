package com.example.p_scanner.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p_scanner.pojo.Item

@Dao
interface ItemDAO {

    @Insert
    fun insertItem(item: Item)

    @Query("select * from ItemsTable")
    fun getAllItems():LiveData<List<Item>>

    @Query("select * from ItemsTable where id=:id")
    fun getItemById(id:String):LiveData<Item>


    @Query("select exists(select * from ItemsTable where id=:id)")
    fun itemIsExists(id : String) : Boolean
}
