package com.example.p_scanner.Pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ProductsTable")
data class Item(@PrimaryKey val id:String, @NonNull val name:String, val description:String, val price:String, val type:ItemType)


enum class ItemType{
    PRODUCT,SERVICE
}