package uz.texnopos.paziylet.di

import android.content.Context
import android.content.SharedPreferences

class Settings(context:Context) {
    companion object{
        const val LogIn="LogIn"
    }
    private val preferences: SharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

    fun setLogIn(){
        preferences.edit().putBoolean(LogIn,true).apply()
    }

    fun checkLogIn():Boolean=preferences.getBoolean(LogIn,false)
}