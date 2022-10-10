package com.example.p_scanner.Pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "ProductsTable")
data class Product(@PrimaryKey val id:String, @NonNull val name:String, val description:String, val price:String)
