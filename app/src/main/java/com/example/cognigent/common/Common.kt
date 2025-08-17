package com.example.cognigent.common

import android.content.Context
import android.content.SharedPreferences

object Common {

    private const val PREF_NAME = "MyAppPreferences"
    private var sharedPref: SharedPreferences? = null

    // ✅ Ensure SharedPreferences is initialized automatically
    private fun getPrefs(context: Context): SharedPreferences {
        if (sharedPref == null) {
            sharedPref = context.applicationContext.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )
        }
        return sharedPref!!
    }

    // ✅ Put data
    fun putString(context: Context, key: String, value: String) {
        getPrefs(context).edit().putString(key, value).apply()
    }

    fun putInt(context: Context, key: String, value: Int) {
        getPrefs(context).edit().putInt(key, value).apply()
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        getPrefs(context).edit().putBoolean(key, value).apply()
    }

    // ✅ Get data
    fun getString(context: Context, key: String, defaultValue: String = ""): String {
        return getPrefs(context).getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        return getPrefs(context).getInt(key, defaultValue)
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        return getPrefs(context).getBoolean(key, defaultValue)
    }

    // ✅ Clear all data
    fun clearAll(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
