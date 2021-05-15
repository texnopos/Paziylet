package uz.texnopos.paziylet.settings

import android.content.Context
import android.content.SharedPreferences

class Settings(context: Context) {
    companion object {
        const val IS_APP_FIRST_LAUNCHED = "isAppFirstLaunched"
    }

    private val preferences: SharedPreferences =
        context.getSharedPreferences("PaziyletUzSharedPreferences", Context.MODE_PRIVATE)

    fun setFirstLaunched() {
        preferences.edit().putBoolean(IS_APP_FIRST_LAUNCHED, false).apply()
    }

    fun isAppFirstLaunched(): Boolean =
        preferences.getBoolean(IS_APP_FIRST_LAUNCHED, true)

}