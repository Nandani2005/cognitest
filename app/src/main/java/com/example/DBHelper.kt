package com.example

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

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

    // SHA-256 password hashing
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Insert new user
    fun insertUser(name: String, email: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", hashPassword(password)) // Store hashed password
        }
        return try {
            db.insertOrThrow("users", null, values) != -1L
        } catch (e: Exception) {
            false
        } finally {
            db.close()
        }
    }

    // Login check
    fun checkLogin(email: String, password: String): Boolean {
        val db = readableDatabase
        val hashedPassword = hashPassword(password)
        val cursor = db.rawQuery(
            "SELECT * FROM users WHERE email=? AND password=?",
            arrayOf(email, hashedPassword)
        )
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }

    // Check if email exists
    fun isEmailRegistered(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email=?", arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // Reset or update password
    fun updatePassword(email: String, newPassword: String): Boolean {
        val db = writableDatabase
        val Password = hashPassword(newPassword)
        val values = ContentValues().apply {
            put("password", Password)
        }
        val rowsUpdated = db.update("users", values, "email = ?", arrayOf(email))
        db.close()
        return rowsUpdated > 0
    }

    //delete id
    fun deleteUserId(email: String): Boolean {
        val db = writableDatabase
        val result = db.delete("users", "email = ?", arrayOf(email))
        db.close()
        return result > 0
    }



    // Get user name and course by email
    fun getUserNameAndCourse(email: String): Pair<String, String>? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name, course FROM users WHERE email=?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val name = cursor.getString(0)
            val course = cursor.getString(1)
            cursor.close()
            db.close()
            Pair(name, course)
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun getPassword(email: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT password FROM users WHERE email=?", arrayOf(email))
        return if (cursor.moveToFirst()) {
            val password = cursor.getString(0)
            cursor.close()
            db.close()
            password
        } else {
            cursor.close()
            db.close()
            null
        }
    }


    // Update only course
    fun updateCourse(email: String, course: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply { put("course", course) }
        val updated = db.update("users", values, "email=?", arrayOf(email))
        db.close()
        return updated > 0
    }

    // Update name and course (basic)
    fun updateUserProfile(email: String, name: String, course: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("course", course)
        }
        val result = db.update("users", values, "email=?", arrayOf(email))
        db.close()
        return result > 0
    }

    // Generic single field update
    fun updateUserField(email: String, field: String, value: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply { put(field, value) }
        val result = db.update("users", values, "email=?", arrayOf(email)) > 0
        db.close()
        return result
    }

    // Update name, course, email, and password together
    fun updateUserProfileWithEmailChange(
        oldEmail: String,
        newEmail: String,
        name: String,
        course: String,
        password: String
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", newEmail)
            put("course", course)
            put("password", hashPassword(password))
        }
        val result = db.update("users", values, "email = ?", arrayOf(oldEmail))
        db.close()
        return result > 0
    }
}
