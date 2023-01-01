package com.example.p_scanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.p_scanner.dao.ItemDAO
import com.example.p_scanner.pojo.Item

@Database(entities = [Item::class] , version = 6)
abstract class ItemsDatabase:RoomDatabase() {
    abstract fun itemDAO():ItemDAO
    companion object{
        @Volatile
        private var INSTANSE: ItemsDatabase? = null
        fun getDatabase(context: Context): ItemsDatabase {
            return INSTANSE ?: synchronized(this){
                val instanse = Room.databaseBuilder(
                    context.applicationContext ,ItemsDatabase::class.java ,"Items Database"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANSE = instanse
                instanse
            }
        }
    }
}