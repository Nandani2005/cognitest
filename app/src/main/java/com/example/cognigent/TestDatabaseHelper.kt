package com.example.cognigent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TestDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "tests.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE tests (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS tests")
        onCreate(db)
    }

    fun insertTest(name: String, date: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("name", name)
        values.put("date", date)
        return db.insert("tests", null, values) != -1L
    }

    fun getAllTests(): List<TestModel> {
        val list = mutableListOf<TestModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM tests", null)
        if (cursor.moveToFirst()) {
            do {
                val test = TestModel(
                    id = cursor.getInt(0),
                    name = cursor.getString(1),
                    date = cursor.getString(2)
                )
                list.add(test)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
