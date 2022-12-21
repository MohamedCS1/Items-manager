package com.example.p_scanner.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p_scanner.Pojo.Item

@Dao
interface ItemDAO {

    @Insert
    fun insertItem(item: Item)

    @Query("select * from ProductsTable")
    fun getAllItems():LiveData<List<Item>>

    @Query("select * from ProductsTable where id=:id")
    fun getItemById(id:String):LiveData<Item>
}
