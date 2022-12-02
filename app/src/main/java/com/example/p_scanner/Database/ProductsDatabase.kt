package com.example.p_scanner.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.p_scanner.DAO.ProductDAO
import com.example.p_scanner.Pojo.Item

@Database(entities = [Item::class] , version = 1)
abstract class ProductsDatabase:RoomDatabase() {
    abstract fun productDAO():ProductDAO
    companion object{
        @Volatile
        private var INSTANSE:ProductsDatabase? = null
        fun getDatabase(context: Context):ProductsDatabase{
            return INSTANSE ?: synchronized(this){
                val instanse = Room.databaseBuilder(
                    context.applicationContext ,ProductsDatabase::class.java ,"Product Database"
                ).build()
                INSTANSE = instanse
                instanse
            }
        }
    }
}