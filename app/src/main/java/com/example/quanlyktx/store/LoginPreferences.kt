package com.example.quanlyktx.store

import android.content.Context
import android.content.SharedPreferences
import com.example.quanlyktx.model.UserModel
import com.example.quanlyktx.model.UserRegModel

class LoginPreferences(context: Context) {
    private val sharedPreferences = "TokenPref"
    private var preferences: SharedPreferences? = null
    init {
        preferences = context.getSharedPreferences(sharedPreferences, android.content.Context.MODE_PRIVATE)
    }

    fun saveInfo(userInfo: UserModel){
        val editor = preferences!!.edit()
        editor.putString("id", userInfo.id)
        editor.putString("name", userInfo.name)
        editor.putString("cls", userInfo.cls)
        editor.putString("date", userInfo.date)
        editor.putString("department", userInfo.department)
        editor.putString("passwd", userInfo.passwd)
        editor.putString("dateJoined", userInfo.dateJoined)
        editor.putString("status", userInfo.status)
        editor.putString("expiry", userInfo.expiry)
        editor.putString("gender", userInfo.gender)
        editor.putString("room", userInfo.room)
        editor.apply()
    }

    fun getUserInfo(): UserModel{
        return UserModel(
            preferences!!.getString("id", ""),
            preferences!!.getString("name", ""),
            preferences!!.getString("date", ""),
            preferences!!.getString("cls", ""),
            preferences!!.getString("department", ""),
            preferences!!.getString("passwd", ""),
            preferences!!.getString("dateJoined", ""),
            preferences!!.getString("status", ""),
            preferences!!.getString("expiry", ""),
            preferences!!.getString("gender", ""),
            preferences!!.getString("room", "")
        )
    }

    fun clearInfo(){
        preferences!!.edit().clear().apply()
    }

    fun setValue(type: String, data: String){
        val editor = preferences!!.edit()
        editor.putString(type, data)
        editor.apply()
    }
}