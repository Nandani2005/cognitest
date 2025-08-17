package com.example.cognigent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*
import java.security.MessageDigest

class QuestionDatabase(context: Context) : SQLiteOpenHelper(context, "quiz.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
        CREATE TABLE questions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            coursetype TEXT,
            queType INTEGER,
            que TEXT,
            opt1 TEXT,
            opt2 TEXT,
            opt3 TEXT,
            opt4 TEXT,
            correctAnswer INTEGER,
            correctAnswerDetails TEXT,
            queImage TEXT,
            title1 TEXT,
            title2 TEXT,
            name1 TEXT,
            name2 TEXT,
            name3 TEXT,
            name4 TEXT,
            value1 TEXT,
            value2 TEXT,
            value3 TEXT,
            value4 TEXT,
            dropdown TEXT
        )
    """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS results")
        onCreate(db)
    }

    val sampleQuestions = listOf(
        QuestionModel(
            1, "What is the largest planet in our Solar System?", null,
            listOf("Earth", "Mars", "Jupiter", "Saturn"), 2,
            "Jupiter is the largest planet in our Solar System."
        ),
        QuestionModel(
            1, "Who invented the telephone?", null,
            listOf("Alexander Graham Bell", "Thomas Edison", "Nikola Tesla", "Isaac Newton"), 0,
            "Alexander Graham Bell is credited with inventing the telephone."
        ),
        QuestionModel(
            2, "What is the capital of France?", R.drawable.acc_logo,
            listOf("Berlin", "Madrid", "Paris", "Rome"), 2,
            "Paris is the capital of France."
        ),
        QuestionModel(
            1, "Which continent is the Sahara Desert located on?", null,
            listOf("Asia", "South America", "Africa", "Australia"), 2,
            "The Sahara Desert is located in northern Africa."
        ),
        QuestionModel(
            3,
            "Match the languages with type",
            null,
            listOf("A-1, B-2, C-3", "A-2, B-1, C-3", "A-3, B-2, C-1", "A-1, B-3, C-2"),
            0,
            "Java is OOP, Python is scripting, C++ is low level",
            matchA = listOf("A. Java", "B. Python", "C. C++"),
            matchB = listOf("1. Object Oriented", "2. Scripting", "3. Low Level")
        )
    )

    fun insertQuestionFromJSON(
        coursetype: String,
        queType: Int,
        que: String,
        opt1: String?,
        opt2: String?,
        opt3: String?,
        opt4: String?,
        correctAnswer: Int,
        correctAnswerDetails: String?,
        queImage: String?,
        title1: String?,
        title2: String?,
        name1: String?,
        name2: String?,
        name3: String?,
        name4: String?,
        value1: String?,
        value2: String?,
        value3: String?,
        value4: String?,
        dropdown: String?
    ): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("coursetype", coursetype)
            put("queType", queType)
            put("que", que)
            put("opt1", opt1)
            put("opt2", opt2)
            put("opt3", opt3)
            put("opt4", opt4)
            put("correctAnswer", correctAnswer)
            put("correctAnswerDetails", correctAnswerDetails)
            put("queImage", queImage)
            put("title1", title1)
            put("title2", title2)
            put("name1", name1)
            put("name2", name2)
            put("name3", name3)
            put("name4", name4)
            put("value1", value1)
            put("value2", value2)
            put("value3", value3)
            put("value4", value4)
            put("dropdown", dropdown)
        }
        return db.insert("questions", null, contentValues)
    }

}
