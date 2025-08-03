package com.example.cognigent.common

import android.content.Context
import android.content.SharedPreferences

object SharedPrefManager {

    private const val PREF_NAME = "MyAppPreferences"
    private lateinit var sharedPref: SharedPreferences

    // ✅ Initialize SharedPreferences (Call in Application class or first activity)
    fun init(context: Context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ✅ Function to store data
    fun putString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    // ✅ Function to get data
    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue) ?: defaultValue
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPref.getInt(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return sharedPref.getBoolean(key, defaultValue)
    }

    // ✅ Clear all data
    fun clearAll() {
        sharedPref.edit().clear().apply()
    }
}
