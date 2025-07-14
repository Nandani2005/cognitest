package com.example

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "users.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                phone TEXT,
                course TEXT
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun insertUser(name: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        return try {
            db.insertOrThrow("users", null, values) != -1L
        } catch (e: Exception) {
            false
        }
    }

    fun checkLogin(email: String, password: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, password)
        )
        val result = cursor.count > 0
        cursor.close()
        return result
    }

    fun getUserDetails(email: String): Pair<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT name, email FROM users WHERE email=?",
            arrayOf(email)
        )
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            val email = cursor.getString(1)
            cursor.close()
            Pair(name, email)
        } else {
            cursor.close()
            null
        }
    }

    fun updateUserField(email: String, field: String, value: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply { put(field, value) }
        return db.update("users", values, "email=?", arrayOf(email)) > 0
    }
    fun isEmailRegistered(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email=?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

} 

