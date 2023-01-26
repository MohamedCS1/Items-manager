package com.example.p_scanner.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.p_scanner.R

class SharedPreference(context: Context) {
    var sharedPreferences: SharedPreferences? = null

    var sharedPreferencesEditor: SharedPreferences.Editor? = null

    var PRIVATE_MODE = 0

    val isSeen = "isSeen"

    fun splashScreenIsSeenNow()
    {
        sharedPreferencesEditor?.putBoolean(isSeen ,true)
        sharedPreferencesEditor?.commit()
    }

    fun splashScreenIsSeen():Boolean{
        return sharedPreferences?.getBoolean(isSeen ,false)!!
    }

    init {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name) ,PRIVATE_MODE)
        sharedPreferencesEditor = sharedPreferences?.edit()
    }
}