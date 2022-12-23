package com.example.p_scanner.pojo

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "ItemsTable")
data class Item(@PrimaryKey val id:String, @NonNull val name:String, val description:String, val price:String, val type:ItemType):Serializable


