package com.example.p_scanner.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.p_scanner.Pojo.Product

@Database(entities = [Product::class] , version = 1)
abstract class ProductsDatabase:RoomDatabase() {

    @Volatile
    private var INSTANSE:ProductsDatabase? = null
    fun getDatabase(context: Context):ProductsDatabase{
        return INSTANSE ?: synchronized(this){
            val instanse = Room.databaseBuilder(
                context.applicationContext ,ProductsDatabase::class.java ,"Product Databse"
            ).build()
            INSTANSE = instanse
            instanse
        }
    }
}