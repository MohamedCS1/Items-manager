package com.example.p_scanner.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.p_scanner.Pojo.Product

@Dao
interface ProductDAO {

    @Insert
    fun insertProduct(product: Product)

    @Query("select * from ProductsTable")
    fun getProduct(id:Long)
}