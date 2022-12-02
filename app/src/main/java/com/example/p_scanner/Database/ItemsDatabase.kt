package com.example.p_scanner.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.p_scanner.DAO.ItemDAO
import com.example.p_scanner.Pojo.Item

@Database(entities = [Item::class] , version = 1)
abstract class ItemsDatabase:RoomDatabase() {
    abstract fun productDAO():ItemDAO
    companion object{
        @Volatile
        private var INSTANSE: ItemsDatabase? = null
        fun getDatabase(context: Context): ItemsDatabase {
            return INSTANSE ?: synchronized(this){
                val instanse = Room.databaseBuilder(
                    context.applicationContext ,ItemsDatabase::class.java ,"Product Database"
                ).build()
                INSTANSE = instanse
                instanse
            }
        }
    }
}