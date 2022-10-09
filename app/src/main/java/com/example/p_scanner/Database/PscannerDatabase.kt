package com.example.p_scanner.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.p_scanner.Pojo.Product

@Database(entities = [Product::class] , version = 1)
abstract class PscannerDatabase:RoomDatabase() {
}