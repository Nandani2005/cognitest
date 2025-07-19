package com.example.cognigent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class QuestionDatabase(context: Context) : SQLiteOpenHelper(context, "quiz.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create questions table with userAnswer column
        db.execSQL("""
            CREATE TABLE questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question TEXT NOT NULL,
                optionA TEXT NOT NULL,
                optionB TEXT NOT NULL,
                optionC TEXT NOT NULL,
                optionD TEXT NOT NULL,
                correctAnswer INTEGER NOT NULL,
                userAnswer INTEGER DEFAULT -1
            )
        """)

        // Create results table
        db.execSQL("""
            CREATE TABLE results (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                score INTEGER NOT NULL,
                timestamp TEXT NOT NULL
            )
        """)

        insertSampleQuestions(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS questions")
        db.execSQL("DROP TABLE IF EXISTS results")
        onCreate(db)
    }

    private fun insertSampleQuestions(db: SQLiteDatabase) {
        val sampleQuestions = listOf(
            QuestionModel(1, "BCA","What is the capital of France?", "London", "Berlin", "Paris", "Rome", 2),
            QuestionModel(2, "BCA","Which number is even?", "3", "7", "10", "5", 2),
            QuestionModel(4, "BCA","Which one is a fruit?", "Potato", "Carrot", "Banana", "Onion", 2) ,
            QuestionModel(5, "BCA","What is the capital of France?", "London", "Berlin", "Paris", "Rome", 2)
        )

        for (q in sampleQuestions) {
            val values = ContentValues().apply {
                put("question", q.questionText)
                put("optionA", q.optionA)
                put("optionB", q.optionB)
                put("optionC", q.optionC)
                put("optionD", q.optionD)
                put("correctAnswer", q.correctIndex)
                put("userAnswer", -1)  // default not attempted
            }
            db.insert("questions", null, values)
        }
    }

    fun getAllQuestions(): List<QuestionModel> {
        val questionList = mutableListOf<QuestionModel>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM questions", null)

        if (cursor.moveToFirst()) {
            do {
                questionList.add(
                    QuestionModel(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        examType = "mba",
                        questionText = cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        optionA = cursor.getString(cursor.getColumnIndexOrThrow("optionA")),
                        optionB = cursor.getString(cursor.getColumnIndexOrThrow("optionB")),
                        optionC = cursor.getString(cursor.getColumnIndexOrThrow("optionC")),
                        optionD = cursor.getString(cursor.getColumnIndexOrThrow("optionD")),
                        correctIndex = cursor.getInt(cursor.getColumnIndexOrThrow("correctAnswer")),
                        Attemp = cursor.getInt(cursor.getColumnIndexOrThrow("userAnswer"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return questionList
    }

    fun saveUserAnswer(questionId: Int, selectedIndex: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userAnswer", selectedIndex)
        }
        db.update("questions", values, "id = ?", arrayOf(questionId.toString()))
    }

    fun saveResult(score: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("score", score)
            put("timestamp", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
        }
        db.insert("results", null, values)
    }

    fun getAllResults(): List<Pair<Int, String>> {
        val results = mutableListOf<Pair<Int, String>>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT score, timestamp FROM results ORDER BY id DESC", null)

        if (cursor.moveToFirst()) {
            do {
                results.add(
                    Pair(
                        cursor.getInt(cursor.getColumnIndexOrThrow("score")),
                        cursor.getString(cursor.getColumnIndexOrThrow("timestamp"))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        return results
    }
}
