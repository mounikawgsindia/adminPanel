package com.wingspan.adminpanel.utils

import android.annotation.SuppressLint
import android.content.Context

class UserPreferences(context: Context) {
    private var sharedPreferences=context.getSharedPreferences("store",Context.MODE_PRIVATE)
    companion object{
        const val KEY_ID="shopkeeperid"
        const val KEY_NAME="username"

        const val IS_LOGGED_IN="isLoggedIn"
    }
    @SuppressLint("CommitPrefEdits")
    fun saveData(id:String, username:String){
        sharedPreferences.edit().apply(){
            putString(KEY_ID,id)
            putString(KEY_NAME,username)
            putBoolean(IS_LOGGED_IN, true)
            apply()
        }
    }
    @SuppressLint("CommitPrefEdits")
    fun logoutShopKeeper(){
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN,false)
    }
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_ID,null)
    }
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME,null)
    }
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(IS_LOGGED_IN, false)
}