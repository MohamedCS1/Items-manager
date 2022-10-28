package com.example.p_scanner.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.p_scanner.Pojo.Product

@Dao
interface ProductDAO {

    @Insert
    fun insertProduct(product: Product)

    @Query("select * from ProductsTable")
    fun getProduct():LiveData<List<Product>>

    @Query("select * from ProductsTable where id=:id")
    fun getProductById(id:String):LiveData<Product>
}